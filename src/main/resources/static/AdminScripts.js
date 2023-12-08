$(async function () {
    await fillHeadbar();
    await fillTheTable();
    getDefaultModal();
})

async function fillHeadbar() {
    let response = await fetch("http://localhost:8080/api/admin/get-auth");
    let currentUser = await response.json()

    let element = document.getElementById("1");
    element.innerHTML = currentUser.username;
    element = document.getElementById("2");
    element.innerHTML = currentUser.roleNames;
}

async function fillTheTable() {
    let table = $('#userInfoTable tbody')
    table.empty()
    let response = await fetch("http://localhost:8080/api/admin/get-users");
    let users = await response.json();
    users.forEach(user => {
        let insert = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.firstName}</td>
                            <td>${user.lastName}</td>
                            <td>${user.age}</td>
                            <td>${user.username}</td>   
                            <td>${user.roleNames}</td>  
                            <td>
                                <button type="button" data-userid="${user.id}" data-action="edit" class="btn btn-info" 
                                data-toggle="modal" data-target="#modalWindow">Edit</button>
                            </td>
                            <td>
                                <button type="button" data-userid="${user.id}" data-action="delete" class="btn btn-danger" 
                                data-toggle="modal" data-target="#modalWindow">Delete</button>
                            </td>      
                        </tr>
                )`;
        table.append(insert)
    })


    $("#userInfoTable").find('button').on('click', (event) => {
        let defaultModal = $('#modalWindow');

        let targetButton = $(event.target);
        let buttonUserId = targetButton.attr('data-userid');
        let buttonAction = targetButton.attr('data-action');

        defaultModal.attr('data-userid', buttonUserId);
        defaultModal.attr('data-action', buttonAction);
        defaultModal.modal('show');
    })
}


async function getDefaultModal() {
    $('#modalWindow').modal({
        keyboard: true,
        backdrop: "static",
        show: false
    }).on("show.bs.modal", (event) => {
        let thisModal = $(event.target);
        let userid = thisModal.attr('data-userid');
        let action = thisModal.attr('data-action');
        switch (action) {
            case 'edit':
                editUser(thisModal, userid);
                break;
            case 'delete':
                deleteUser(thisModal, userid);
                break;
        }
    }).on("hidden.bs.modal", (e) => {
        let thisModal = $(e.target);
        thisModal.find('.modal-title').html('');
        thisModal.find('.modal-body').html('');
        thisModal.find('.modal-footer').html('');
    })
}


async function editUser(modal, id) {
    let resp = await fetch("http://localhost:8080/api/admin/find/" + id);
    let user = resp.json();

    modal.find('.modal-title').html('Edit user');

    let editButton = `<button  class="btn btn-outline-success" id="editButton">Edit</button>`;
    let closeButton = `<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>`
    modal.find('.modal-footer').append(editButton);
    modal.find('.modal-footer').append(closeButton);

    user.then(user => {
        let bodyForm = `
            <form class="form-group" id="editUser">
                <input class="form-control" type="number" id="id" name="id" value="${user.id}" readonly required><br>
                <input class="form-control" type="text" id="firstName" name="firstName" value="${user.firstName}" required><br>
                <input class="form-control" type="text" id="lastName" name="lastName" value="${user.lastName}" required><br>
                <input class="form-control" id="age" type="number" value="${user.age}" required><br>
                <input class="form-control" type="text" id="username" name="username" value="${user.username}" required><br>
                <input class="form-control" type="password" id="password" name="password" disabled>
                <div class="form-select">
                    <label for="roleEdit">Role</label>
                    <select multiple class="form-control"
                            id="roleEdit" name="roleEdit">
                        <option
                                name="ADMIN"
                                value="ADMIN">ADMIN
                        </option>
                        <option
                                name="USER"
                                value="USER">USER
                        </option>
                    </select>
                </div>
            </form>
        `;
        modal.find('.modal-body').append(bodyForm);
    })

    $("#editButton").on('click', async () => {
        let id = parseInt(modal.find("#id").val());
        let firstName = modal.find("#firstName").val().trim();
        let lastName = modal.find("#lastName").val().trim();
        let age = parseInt(modal.find("#age").val());
        let username = modal.find("#username").val().trim();
        let password = modal.find("#password").val().trim();
        let role = modal.find("#roleEdit").val();
        let data = {
            id: id,
            firstName: firstName,
            lastName: lastName,
            age: age,
            username: username,
            password: password,
            roleNames: role
        }
        await fetch("http://localhost:8080/api/admin/update", {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data)
        });
        await fillTheTable();
        modal.modal('hide');
    })
}

async function deleteUser(modal, id) {
    let resp = await fetch("http://localhost:8080/api/admin/find/" + id);
    let user = resp.json();

    modal.find('.modal-title').html('Delete user');

    let deleteButton = `<button  class="btn btn-outline-success" id="deleteButton">Delete</button>`;
    let closeButton = `<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>`

    modal.find('.modal-footer').append(deleteButton);
    modal.find('.modal-footer').append(closeButton);

    user.then(user => {
        console.log(user)
        let bodyForm = `
            <form class="form-group" id="deleteUser">
                <input class="form-control" type="number" id="id" name="id" value="${user.id}" readonly required><br>
                <input class="form-control" type="text" id="firstName" name="firstName" value="${user.firstName}" readonly required><br>
                <input class="form-control" type="text" id="lastName" name="lastName" value="${user.lastName}" readonly required><br>
                <input class="form-control" id="age" type="number" value="${user.age}" required><br>
                <input class="form-control" type="text" id="username" name="username" value="${user.username}" readonly required><br>
                <input class="form-control" type="password" id="password" name="password" disabled>                
            </form>
        `;
        modal.find('.modal-body').append(bodyForm);
    });
    $("#deleteButton").on('click', async () => {

        await fetch("http://localhost:8080/api/admin/delete/" + id, {
            method: 'DELETE'
        });

        fillTheTable();
        modal.modal('hide');
    });
}

async function createUser() {
    let newUserForm = $('#newUserForm')
    let bodyForm = `
            <form class="text-center pt-4 font-weight-bold" id="newUser">
                <input class="mb-3" type="number" id="id" name="id" value="" placeholder="id" readonly required><br>
                <input class="mb-3" type="text" id="firstName" name="firstName" value="" placeholder="firstName" required><br>
                <input class="mb-3" type="text" id="lastName" name="lastName" value="" placeholder="lastName" required><br>
                <input class="mb-3" id="age" type="number" value="" placeholder="age" required><br>
                <input class="mb-3" type="text" id="username" name="username" value="" placeholder="username" required><br>
                <input class="mb-3" type="password" id="password" name="password" value="" placeholder="password" required>
                <div class="form-select">
                    <label for="newUser">Role</label>
                    <select multiple class="form-control"
                            id="newUser" name="newUser">
                        <option
                                name="ADMIN"
                                value="ADMIN">ADMIN
                        </option>
                        <option
                                name="USER"
                                value="USER">USER
                        </option>
                    </select>
                </div>
            </form>
        `;
    let button = `<button  class="btn btn-success text-wrap btn-lg col-2 text-center" id="createButton">Add user</button>`;
    newUserForm.append(bodyForm)
    newUserForm.append(button)
    $("#createButton").on('click', async () => {
        let firstName = newUserForm.find("#firstName").val().trim();
        let lastName = newUserForm.find("#lastName").val().trim();
        let age = parseInt(newUserForm.find("#age").val());
        let username = newUserForm.find("#username").val().trim();
        let password = newUserForm.find("#password").val().trim();
        let role = newUserForm.find("#roleEdit").val();
        let data = {
            id: null,
            firstName: firstName,
            lastName: lastName,
            age: age,
            username: username,
            password: password,
            roleNames: role
        }
        await fetch("http://localhost:8080/api/admin/add", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data)
        });
        await fillTheTable();
    })
}