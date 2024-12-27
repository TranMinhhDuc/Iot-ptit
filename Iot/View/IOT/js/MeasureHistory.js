let STT = 1;
let currentPage = 0;
let pageSize = 10;
let totalPage = 0;
let searchBy = "";
let searchValue = "";
let sortBy = "";
let sortValue = "";

const inSearchBy = document.getElementById('searchBy');
const deviceNameSearch = document.getElementById('deviceNameSearch');
const dateSearch = document.getElementById('dateSearch');
const prevPageBtn = document.getElementById('prev-page');
const nextPageBtn = document.getElementById('next-page');
const paginationInfo = document.getElementById('pagination-info');

function resetKey() {
    STT = 1;
    currentPage = 0;
    totalPage = 0;
}
inSearchBy.addEventListener('change', function() {
    if (inSearchBy.value === 'date') {
        dateSearch.classList.remove('d-none');
        deviceNameSearch.classList.add('d-none');
    }else {
        deviceNameSearch.classList.remove('d-none'); 
        dateSearch.classList.add('d-none');
    }
});

function getData() {
    const url = `http://localhost:8080/measure/measure-page?page=${currentPage}&searchBy=${searchBy}&searchValue=${searchValue}&pageSize=${pageSize}&sortBy=${sortBy}&sortValue=${sortValue}`;
    fetch(url)
        .then(response => response.json()) 
        .then(data => {
            const measureHistory = data.content;
            totalPage = data.totalPages;
            currentPage = data.number;
            const table = document.getElementById('table-body');
                
            let rows = '';

            for(let i = 0; i < measureHistory.length; i++) {
                rows += `
                    <tr>
                        <td>${STT++}</td>
                        <td>${measureHistory[i].temperature}</td>
                        <td>${measureHistory[i].humidity}</td>
                        <td>${measureHistory[i].bright}</td>
                        <td>${measureHistory[i].pressure}</td>
                        <td>${measureHistory[i].measureDate}</td>
                        <td>${measureHistory[i].measureTime}</td>
                    </tr>
                `;
            }

        table.innerHTML = rows;
        updatePaginationControls();
        }) .catch(error => console.error('Error:', error));
}

function updatePaginationControls() {
    paginationInfo.textContent = `Page ${currentPage + 1} of ${totalPage}`;
    prevPageBtn.disabled = currentPage === 0;
    nextPageBtn.disabled = currentPage >= totalPage - 1;
}

prevPageBtn.addEventListener('click', function () {
    if (currentPage > 0) {
        currentPage--;
        STT = currentPage * pageSize + 1;
        getData();
    }
});

nextPageBtn.addEventListener('click', function () {
    if (currentPage < totalPage - 1) {
        currentPage++;
        STT = currentPage * pageSize + 1;
        getData();
    }
});

document.getElementById('searchBtn').addEventListener('click', function() {
    searchBy = inSearchBy.value;
    if(searchBy !='date'){
        searchValue = document.getElementById('searchName').value.trim();
        resetKey();
        getData();
    }else {
        let searchDate = document.getElementById('searchDate').value.trim();
        let searchTime = document.getElementById('searchTime').value.trim();
        if(searchDate === '' && searchTime === '') {
            alert('Hãy nhập thông tin tìm kiếm!');
        }else if(searchDate === ''){
            searchValue = searchTime;
            resetKey();
            getData();
        }else if(searchTime === '') {
            searchValue = searchDate;
            resetKey();
            getData();
        }else {
            searchValue = searchDate+ 'T' + searchTime;
            resetKey();
            getData();
        }
    }
})

document.getElementById('sortBtn').addEventListener('click', function() {
    sortBy = document.getElementById('sortBy').value;
    sortValue = document.getElementById('sortValue').value;
    console.log(sortValue)
    resetKey();
    getData();
})
document.getElementById('size').addEventListener('change', function() {
    pageSize = document.getElementById('size').value;
    resetKey();
    getData();
})
getData()