const API_URL = "http://localhost:8081/api/classrooms";
const classroomForm = document.getElementById("classroomForm");
const classroomBody = document.getElementById("classroomBody");
const saveBtn = document.getElementById("saveBtn");
let editingId = null;

// 🔹 Cargar aulas al inicio
document.addEventListener("DOMContentLoaded", loadClassrooms);

// 🔹 Enviar formulario
classroomForm.addEventListener("submit", async (e) => {
  e.preventDefault();

  const classroom = {
    name: document.getElementById("name").value,
    capacity: parseInt(document.getElementById("capacity").value),
    location: document.getElementById("location").value,
    state: document.getElementById("state").value
  };

  if (editingId) {
    await fetch(`${API_URL}/${editingId}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(classroom)
    });
  } else {
    await fetch(API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(classroom)
    });
  }

  classroomForm.reset();
  editingId = null;
  saveBtn.textContent = "Add Classroom";
  loadClassrooms();
});

// 🔹 Cargar aulas
async function loadClassrooms() {
  const res = await fetch(API_URL);
  const data = await res.json();

  classroomBody.innerHTML = "";

  data.forEach(c => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${c.id}</td>
      <td>${c.name}</td>
      <td>${c.capacity}</td>
      <td>${c.location}</td>
      <td>${c.state}</td>
      <td>
        <button class="edit" onclick="editClassroom(${c.id})">✏️</button>
        <button class="delete" onclick="deleteClassroom(${c.id})">🗑️</button>
      </td>
    `;
    classroomBody.appendChild(tr);
  });
}

// 🔹 Editar aula
async function editClassroom(id) {
  const res = await fetch(`${API_URL}/${id}`);
  const c = await res.json();

  document.getElementById("name").value = c.name;
  document.getElementById("capacity").value = c.capacity;
  document.getElementById("location").value = c.location;
  document.getElementById("state").value = c.state;

  editingId = id;
  saveBtn.textContent = "Update Classroom";
}

// 🔹 Eliminar aula
async function deleteClassroom(id) {
  if (confirm("Are you sure you want to delete this classroom?")) {
    await fetch(`${API_URL}/${id}`, { method: "DELETE" });
    loadClassrooms();
  }
}
