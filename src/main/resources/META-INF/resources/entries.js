const URL = 'http://localhost:8080';
const FORM_MODES = { NEW: 'new', EDIT: 'edit' };
let formMode = FORM_MODES.NEW;
let entryForm;
let entries = [];
let tags = [];
let categories = [];

const dateAndTimeToDate = (dateString, timeString) => {
  return new Date(`${dateString}T${timeString}Z`).toISOString();
};

const saveEntry = (e) => {
  e.preventDefault();
  const formData = new FormData(e.target);
  const entry = {};
  entry['checkIn'] = dateAndTimeToDate(formData.get('checkInDate'), formData.get('checkInTime'));
  entry['checkOut'] = dateAndTimeToDate(formData.get('checkOutDate'), formData.get('checkOutTime'));
  entry['category'] = { id: formData.get('category') };
  entry['tags'] = formData.getAll('tags').map((tag) => ({ id: tag }));

  if (formMode === FORM_MODES.NEW) {
    fetch(`${URL}/entries`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(entry)
    }).then((result) => {
      if (result.status < 300) {
        result.json().then((entry) => {
          entries.push(entry);
          renderEntries();
        });
      }
    });
  } else if (formMode === FORM_MODES.EDIT) {
    fetch(`${URL}/entries/${formData.get('id')}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(entry)
    }).then((result) => {
      result.json().then((entry) => {
        entries = entries.map((c) => c.id === entry.id ? entry : c);
        renderEntries();
      });
    });
  };

  formMode = FORM_MODES.NEW;
  e.target.reset();
};

const deleteEntry = (id) => {
  fetch(`${URL}/entries/${id}`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json'
    },
  }).then(() => {
    entries = entries.filter((entry) => entry.id !== id);
    renderEntries();
  });
}

const editEntry = (id) => {
  formMode = FORM_MODES.EDIT;
  const entry = entries.find((entry) => entry.id === id);
  const checkIn = new Date(`${entry.checkIn}Z`);
  const checkOut = new Date(`${entry.checkOut}Z`);
  const tagCheckboxes = entryForm.querySelectorAll('input[name=tags]');
  tagCheckboxes.forEach((tagCheckbox) => {
    const foundTag = entry.tags.find((tag) => tag.id == tagCheckbox.value);
    if (foundTag) {
      tagCheckbox.checked = true;
    } else {
      tagCheckbox.checked = false;
    }
  });
  entryForm.querySelector('input[name=id]').value = entry.id;
  entryForm.querySelector('select[name=category]').value = entry.category.id;
  entryForm.querySelector('input[name=checkInDate]').value = checkIn.toISOString().split('T')[0];
  entryForm.querySelector('input[name=checkInTime]').value = checkIn.toISOString().split('T')[1].replace('Z', '');
  entryForm.querySelector('input[name=checkOutDate]').value = checkOut.toISOString().split('T')[0];
  entryForm.querySelector('input[name=checkOutTime]').value = checkOut.toISOString().split('T')[1].replace('Z', '');
};

const indexEntries = () => {
  fetch(`${URL}/entries`, {
    method: 'GET'
  }).then((result) => {
    result.json().then((result) => {
      entries = result;
      renderEntries();
    });
  });
  renderEntries();
};

const indexTags = () => {
  fetch(`${URL}/tags`, {
    method: 'GET'
  }).then((result) => {
    result.json().then((result) => {
      tags = result;
      renderTags();
    });
  });
  renderTags();
};

const indexCategories = () => {
  fetch(`${URL}/categories`, {
    method: 'GET'
  }).then((result) => {
    result.json().then((result) => {
      categories = result;
      renderCategories();
    });
  });
  renderCategories();
};

const createCell = (text) => {
  const cell = document.createElement('td');
  cell.innerText = text;
  return cell;
};

const createActionsCell = (id) => {
  const cell = document.createElement('td');
  const deleteButton = document.createElement('button');
  deleteButton.innerText = "Delete";
  deleteButton.addEventListener('click', () => {
    deleteEntry(id);
  });
  cell.appendChild(deleteButton);
  const editButton = document.createElement('button');
  editButton.innerText = "Edit";
  editButton.addEventListener('click', () => {
    editEntry(id);
  });
  cell.appendChild(editButton);
  return cell;
}

const renderTags = () => {
  const tagsSelect = document.querySelector('#tagsSelect');

  if (tags.length > 0) {
    tagsSelect.replaceChildren(...tags.map((tag) => {
      const checkbox = document.createElement('input');
      checkbox.setAttribute('name', 'tags');
      checkbox.setAttribute('value', tag.id);
      checkbox.setAttribute('type', 'checkbox');
      checkbox.setAttribute('id', `tag${tag.id}Input`);
      const label = document.createElement('label');
      label.setAttribute('for', `tag${tag.id}Input`);
      label.innerText = tag.title;
      return [checkbox, label];
    }).flat());
  }
}

const renderCategories = () => {
  const categorySelect = document.querySelector('#categorySelect');

  if (categories.length > 0) {
    categorySelect.replaceChildren(...categories.map((category) => {
      const option = document.createElement('option');
      option.setAttribute('value', category.id);
      option.innerText = category.title;
      return option;
    }));
  }
}

const renderEntries = () => {
  const display = document.querySelector('#entryDisplay');
  display.innerHTML = '';
  entries.forEach((entry) => {
    const row = document.createElement('tr');
    row.appendChild(createCell(entry.id));
    row.appendChild(createCell(entry.category.title));
    row.appendChild(createCell(entry.tags.map((tag) => tag.title).join(', ')));
    row.appendChild(createCell(new Date(entry.checkIn).toLocaleString()));
    row.appendChild(createCell(new Date(entry.checkOut).toLocaleString()));
    row.appendChild(createActionsCell(entry.id));
    display.appendChild(row);
  });
};

document.addEventListener('DOMContentLoaded', function () {
  entryForm = document.querySelector('#entryForm');
  entryForm.addEventListener('submit', saveEntry);
  indexEntries();
  indexTags();
  indexCategories();
});