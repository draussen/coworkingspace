const URL = 'http://localhost:8080';
const FORM_MODES = { NEW: 'new', EDIT: 'edit' };
let formMode = FORM_MODES.NEW;
let tagForm;
let tags = [];

const saveTag = (e) => {
  e.preventDefault();
  const formData = new FormData(e.target);
  const tag = {};
  tag['title'] = formData.get('title');

  if (formMode === FORM_MODES.NEW) {
    fetch(`${URL}/tags`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(tag)
    }).then((result) => {
      result.json().then((tag) => {
        tags.push(tag);
        renderTags();
      });
    });
  } else if (formMode === FORM_MODES.EDIT) {
    fetch(`${URL}/tags/${formData.get('id')}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(tag)
    }).then((result) => {
      result.json().then((tag) => {
        tags = tags.map((c) => c.id === tag.id ? tag : c);
        renderTags();
      });
    });
  };

  formMode = FORM_MODES.NEW;
  e.target.reset();
};

const deleteTag = (id) => {
  fetch(`${URL}/tags/${id}`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json'
    },
  }).then(() => {
    tags = tags.filter((tag) => tag.id !== id);
    renderTags();
  });
}

const editTag = (id) => {
  formMode = FORM_MODES.EDIT;
  const tag = tags.find((tag) => tag.id === id);
  tagForm.querySelector('input[name=id]').value = tag.id;
  tagForm.querySelector('input[name=title]').value = tag.title;
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
    deleteTag(id);
  });
  cell.appendChild(deleteButton);
  const editButton = document.createElement('button');
  editButton.innerText = "Edit";
  editButton.addEventListener('click', () => {
    editTag(id);
  });
  cell.appendChild(editButton);
  return cell;
}

const renderTags = () => {
  const display = document.querySelector('#tagDisplay');
  display.innerHTML = '';
  tags.forEach((tag) => {
    const row = document.createElement('tr');
    row.appendChild(createCell(tag.id));
    row.appendChild(createCell(tag.title));
    row.appendChild(createActionsCell(tag.id));
    display.appendChild(row);
  });
};

document.addEventListener('DOMContentLoaded', function () {
  tagForm = document.querySelector('#tagForm');
  tagForm.addEventListener('submit', saveTag);
  indexTags();
});