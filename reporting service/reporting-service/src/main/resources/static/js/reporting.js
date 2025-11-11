// URL base del backend
const API_BASE = "http://localhost:8083/api/reports";

let currentChart = null; // Guardar el gráfico actual para poder destruirlo antes de dibujar otro

// Función principal de visualización
function showResult(title, data) {
    const resultDiv = document.getElementById("reportResult");
    resultDiv.innerHTML = `
        <h3>${title}</h3>
        <canvas id="reportChart"></canvas>
        <div id="dataTable"></div>
    `;

    if (!data || data.length === 0) {
        resultDiv.innerHTML += `<p>No hay datos disponibles.</p>`;
        return;
    }

    // Preparar datos para la gráfica
    const keys = Object.keys(data[0]);
    const labels = [];
    const values = [];

    // Detectar campos relevantes automáticamente
    const labelKey = keys.find(k => /program|classroom|hour|week|month/i.test(k)) || keys[0];
    const valueKey = keys.find(k => /count|frequency/i.test(k)) || keys[1];

    data.forEach(item => {
        labels.push(item[labelKey]);
        values.push(item[valueKey]);
    });

    // Crear el gráfico
    const ctx = document.getElementById("reportChart").getContext("2d");
    if (currentChart) currentChart.destroy(); // Evita superposición de gráficos

    const chartType = /frequency|count/i.test(valueKey)
        ? (data.length > 5 ? "bar" : "pie")
        : "bar";

    currentChart = new Chart(ctx, {
        type: chartType,
        data: {
            labels: labels,
            datasets: [{
                label: title,
                data: values,
                backgroundColor: [
                    "#FFCC00", "#1C1C1C", "#FFD700", "#333333", "#F4D03F"
                ],
                borderColor: "#000",
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { display: true, position: "bottom" },
                title: { display: true, text: title, font: { size: 18 } }
            },
            scales: chartType === "bar" ? {
                y: {
                    beginAtZero: true,
                    title: { display: true, text: "Cantidad" }
                },
                x: {
                    title: { display: true, text: labelKey }
                }
            } : {}
        }
    });

    // ---- Agregar tabla debajo del gráfico ----
    const dataTable = document.getElementById("dataTable");
    const table = document.createElement("table");
    table.classList.add("styled-table");

    // Cabecera
    const headerRow = document.createElement("tr");
    keys.forEach(k => {
        const th = document.createElement("th");
        th.textContent = k.toUpperCase();
        headerRow.appendChild(th);
    });
    table.appendChild(headerRow);

    // Filas de datos
    data.forEach(row => {
        const tr = document.createElement("tr");
        keys.forEach(k => {
            const td = document.createElement("td");
            td.textContent = row[k];
            tr.appendChild(td);
        });
        table.appendChild(tr);
    });

    dataTable.appendChild(table);
}

// ---- Funciones para consumir los endpoints ----

// 1️⃣ Horas pico
function getPeakHours() {
    fetch(`${API_BASE}/peak-hours`)
        .then(response => response.json())
        .then(data => showResult("⏰ Horario con mayor frecuencia de préstamo", data))
        .catch(error => console.error("Error:", error));
}

// 2️⃣ Horas bajas
function getLowHours() {
    fetch(`${API_BASE}/low-hours`)
        .then(response => response.json())
        .then(data => showResult("🌙 Horario con menor frecuencia de préstamo", data))
        .catch(error => console.error("Error:", error));
}

// 3️⃣ Reporte semanal por programa
function getWeeklyByProgram() {
    fetch(`${API_BASE}/weekly-by-program`)
        .then(response => response.json())
        .then(data => showResult("📆 Reportes semanales por programa", data))
        .catch(error => console.error("Error:", error));
}

// 4️⃣ Reporte mensual por programa
function getMonthlyByProgram() {
    fetch(`${API_BASE}/monthly-by-program`)
        .then(response => response.json())
        .then(data => showResult("📊 Reportes mensuales por programa", data))
        .catch(error => console.error("Error:", error));
}

// 5️⃣ Frecuencia de salones
function getClassroomFrequency() {
    fetch(`${API_BASE}/classroom-frequency`)
        .then(response => response.json())
        .then(data => showResult("🏫 Frecuencia de préstamo de las salas", data))
        .catch(error => console.error("Error:", error));
}
