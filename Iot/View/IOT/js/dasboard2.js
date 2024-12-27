let measurementChart;
function updateChart(temperature) {
    if (measurementChart) {
        measurementChart.data.labels.push(new Date().toLocaleTimeString());
        measurementChart.data.datasets[0].data.push(temperature);

        if (measurementChart.data.labels.length > 20) {
            measurementChart.data.labels.shift();
            measurementChart.data.datasets.forEach(dataset => dataset.data.shift());
        }

        measurementChart.update();
    }
}

function updateChartFromAPI() {
    // Lấy dữ liệu từ API
    fetch('http://localhost:8080/measure/measure-page?pageSize=15')
        .then(response => response.json())
        .then(data => {
            // Dữ liệu trả về từ API
            const measurements = data.content;
            const xValues = [];
            const temperatureData = [];

            // Lặp qua dữ liệu để chuẩn bị cho biểu đồ
            measurements.forEach(measurement => {
                const time = `${measurement.measureTime}`; // Định dạng thời gian
                xValues.push(time); // Thêm vào labels (trục X)
                temperatureData.push(measurement.temperature); // Thêm nhiệt độ vào datasets
            });

            // Cập nhật biểu đồ với dữ liệu mới
            if (measurementChart) {
                measurementChart.data.labels = xValues;
                measurementChart.data.datasets[0].data = temperatureData;

                measurementChart.update(); // Cập nhật lại biểu đồ
            }
        })
        .catch(error => {
            console.error('Error fetching data from API:', error);
        });
}
updateChartFromAPI()
// WebSocket kết nối và nhận dữ liệu
const socket = new WebSocket("ws://localhost:8080/dashboard");

socket.addEventListener("open", () => {
    console.log("WebSocket connected to ws://localhost:8080/dashboard");
});

socket.addEventListener("message", (event) => {
    try {
        if (event.data.length > 5) { // Kiểm tra độ dài chuỗi
            const data = JSON.parse(event.data);
            const { temperature, humidity, bright } = data;
    
            // Cập nhật giao diện
            updateMeasurementDisplay(temperature, humidity, bright);
            updateChart(temperature, humidity, bright);
        }
    } catch (error) {
        console.error("Error parsing WebSocket data:", error);
    }
});

socket.addEventListener("error", (error) => {
    console.error("WebSocket error:", error);
});

socket.addEventListener("close", () => {
    console.log("WebSocket connection closed.");
});

// Khởi tạo biểu đồ khi DOM đã sẵn sàng
document.addEventListener("DOMContentLoaded", function () {
    const ctx = document.getElementById("measurementChart").getContext("2d");
    measurementChart = new Chart(ctx, {
        type: "line",
        data: {
            labels: [],
            datasets: [
                { label: "Nhiệt độ (℃)", data: [], borderColor: "red", fill: false },
                { label: "Độ ẩm (%)", data: [], borderColor: "blue", fill: false },
                { label: "Độ sáng (lux)", data: [], borderColor: "rgb(150, 156, 0)", fill: false }
            ]
        },
        options: {
            legend: {
                display: true,
                position: 'bottom'
            }
        }
    });

    // Điều khiển nút bật/tắt
    setupControlButtons();
});

function updateMeasurementDisplay(temp) {
    // temperature
    const temperatureIcon = document.getElementById("temperature-icon");
    const temperatureValue = document.getElementById("temperature-value");
    temperatureValue.textContent = `${temp}`;
    if (temp >= 70) {
        temperatureIcon.style.color = "red";
        temperatureIcon.className = "fas fa-thermometer-full";
    } else if (temp >= 50) {
        temperatureIcon.style.color = "orange";
        temperatureIcon.className = "fas fa-thermometer-three-quarters";
    } else if (temp >= 30) {
        temperatureIcon.style.color = "yellow";
        temperatureIcon.className = "fas fa-thermometer-half";
    } else {
        temperatureIcon.style.color = "blue";
        temperatureIcon.className = "fas fa-thermometer-quarter";
    }

}

let temperature = 30;
updateMeasurementDisplay(temperature);

// Chart

document.addEventListener("DOMContentLoaded", function(){
    fetch('/data/measurementsHistory.json')
    .then(response => response.json())
    .then(data => {
        const xValues = data.Labels;
        const temperatureData = data.temperature;
        new Chart("measurementChart", {
            type: "line",
            data: {
            labels: xValues,
            datasets: [{ 
                label: "Nhiệt độ (℃)",
                data: temperatureData,
                borderColor: "red",
                fill: false
            }]
            },
            options: {
            legend: {
                display: true, 
                position: 'bottom'
            }
            }
        });
    })
})

document.addEventListener("DOMContentLoaded", function () {
    const buldOnButton = document.querySelector('.buldOn');
    const buldOffButton = document.querySelector('.buldOff');
    const fanOnButton = document.querySelector('.fanOn');
    const fanOffButton = document.querySelector('.fanOff');
    const airOnButton = document.querySelector('.airOn');
    const airOffButton = document.querySelector('.airOff');

    // Đặt trạng thái mặc định
    buldOffButton.classList.add('active');
    fanOffButton.classList.add('active');
    airOffButton.classList.add('active');

    // Lệnh điều khiển
    const meson1 = "ON1";
    const mesoff1 = "OFF1";
    const meson2 = "ON2";
    const mesoff2 = "OFF2";
    const messon3 = "ON3";
    const messoff3 = "OFF3";

    // Điều khiển bật/tắt đèn
    buldOnButton.addEventListener('click', function () {
        socket.send(meson1);
    });

    buldOffButton.addEventListener('click', function () {
        socket.send(mesoff1);
    });

    // Điều khiển bật/tắt quạt
    fanOnButton.addEventListener('click', function () {
        socket.send(meson2);
    });

    fanOffButton.addEventListener('click', function () {
        socket.send(mesoff2);
    });

    // Điều khiển bật/tắt điều hòa
    airOnButton.addEventListener('click', function () {
        socket.send(messon3);
    });

    airOffButton.addEventListener('click', function () {
        socket.send(messoff3);
    });

    socket.addEventListener('message', (event) => {
        try {
            const response = event.data;
            console.log(response);
            if(response === "1ON") {
                buldOnButton.classList.add('active');
                buldOffButton.classList.remove('active');
                buldIcon.style.color = "rgba(192, 192, 33, 0.801)";
            }
            if(response === "1OFF"){
                buldOffButton.classList.add('active');
                buldOnButton.classList.remove('active');
                buldIcon.style.color = "black";
            }

            if(response === "2ON"){
                fanOnButton.classList.add('active');
                fanOffButton.classList.remove('active');
                fanIcon.style.color = "green";
                fanIcon.classList.add('spin');
            }
            if(response === "2OFF") {
                fanOffButton.classList.add('active');
                fanOnButton.classList.remove('active');
                fanIcon.style.color = "black";
                fanIcon.classList.remove('spin');
            }

            if(response === "3ON") {
                airOnButton.classList.add('active');
                airOffButton.classList.remove('active');
                airIcon.style.color = "lightblue";
            }
            if(response === "3OFF"){
                airOffButton.classList.add('active');
                airOnButton.classList.remove('active');
                airIcon.style.color = "black";
            }
        }catch (error) {
            console.error("Error processing backend response:", error);
        }})
});
