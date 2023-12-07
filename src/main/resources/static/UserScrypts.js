'use strict'

const userUrl = 'http://localhost:8080/api/user/getUserPage'

fillHeadbar();
fillTheTable();

async function fillHeadbar() {
    let response = await fetch(userUrl);
    let currentUser = await response.json()

    let element = document.getElementById("1");
    element.innerHTML = currentUser.username;
    element = document.getElementById("2");
    element.innerHTML = currentUser.roleNames;
}

async function fillTheTable() {
    let table = $('#userInfoTable tbody')
    table.empty()
    let response = await fetch(userUrl);
    let user = await response.json();
    let insert = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.firstName}</td>
                            <td>${user.lastName}</td>
                            <td>${user.age}</td>
                            <td>${user.username}</td>   
                            <td>${user.roleNames}</td>        
                        </tr>
                )`;
    table.append(insert)



}
