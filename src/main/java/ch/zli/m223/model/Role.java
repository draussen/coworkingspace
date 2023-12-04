package ch.zli.m223.model;

import javax.persistence.*;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Entity
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(readOnly = true)
  private Long id;

  @Column(nullable = false)
  private String name;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
