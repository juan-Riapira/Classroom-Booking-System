// URL base del backend
const API_BASE = "http://localhost:8083/api/reports";

// Función para mostrar resultados en el contenedor
function showResult(title, data) {
    const resultDiv = document.getElementById("reportResult");
    resultDiv.innerHTML = `<h3>${title}</h3>`;

    if (!data || data.length === 0) {
        resultDiv.innerHTML += `<p>No data found.</p>`;
        return;
    }

    // Crear una tabla simple con los datos recibidos
    const table = document.createElement("table");
    table.border = "1";
    table.style.width = "100%";
    table.style.marginTop = "10px";
    table.style.borderCollapse = "collapse";

    // Encabezados de la tabla (keys del primer objeto)
    const headerRow = document.createElement("tr");
    Object.keys(data[0]).forEach(key => {
        const th = document.createElement("th");
        th.textContent = key;
        th.style.padding = "8px";
        th.style.backgroundColor = "#f4d03f";
        th.style.color = "#000";
        headerRow.appendChild(th);
    });
    table.appendChild(headerRow);

    // Filas de datos
    data.forEach(item => {
        const row = document.createElement("tr");
        Object.values(item).forEach(value => {
            const td = document.createElement("td");
            td.textContent = value;
            td.style.padding = "8px";
            row.appendChild(td);
        });
        table.appendChild(row);
    });

    resultDiv.appendChild(table);
}

// ---- Funciones para consumir los endpoints ----

// 1️⃣ Horas pico
function getPeakHours() {
    fetch(`${API_BASE}/peak-hours`)
        .then(response => response.json())
        .then(data => showResult("Horario con mayor frecuencia de préstamo", data))
        .catch(error => console.error("Error:", error));
}

// 2️⃣ Horas bajas
function getLowHours() {
    fetch(`${API_BASE}/low-hours`)
        .then(response => response.json())
        .then(data => showResult("Horario con menor frecuencia de préstamo", data))
        .catch(error => console.error("Error:", error));
}

// 3️⃣ Reporte semanal por programa
function getWeeklyByProgram() {
    fetch(`${API_BASE}/weekly-by-program`)
        .then(response => response.json())
        .then(data => showResult("Reportes semanales por programa de los prestamos realizados.", data))
        .catch(error => console.error("Error:", error));
}

// 4️⃣ Reporte mensual por programa
function getMonthlyByProgram() {
    fetch(`${API_BASE}/monthly-by-program`)
        .then(response => response.json())
        .then(data => showResult("Reportes mensuales por programa de los prestamos realizados.", data))
        .catch(error => console.error("Error:", error));
}

// 5️⃣ Frecuencia de salones
function getClassroomFrequency() {
    fetch(`${API_BASE}/classroom-frequency`)
        .then(response => response.json())
        .then(data => showResult("Frecuencia de préstamo de las salas", data))
        .catch(error => console.error("Error:", error));
}
