$(async function () {
    await getTableWithUsers();
    getNewUserForm();
    getDefaultModal();
    addNewUser();
})


const userFetchService = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Referer': null
    },
    // bodyAdd : async function(user) {return {'method': 'POST', 'headers': this.head, 'body': user}},
    findAllUsers: async () => await fetch('/api/users'),
    findOneUser: async (id) => await fetch('api/users/' + id),
    addNewUser: async (user) => await fetch('/api/users', {
        method: 'POST',
        headers: userFetchService.head,
        body: JSON.stringify(user)
    }),
    updateUser: async (user, id) => await fetch('/api/users/${id}', {
        method: 'PUT',
        headers: userFetchService.head,
        body: JSON.stringify(user)
    }),
    deleteUser: async (id) => await fetch('/api/users/${id}', {method: 'DELETE', headers: userFetchService.head})
}

async function getTableWithUsers() {
    let div = $('#mainTableWithUsers');
    div.empty();

    await userFetchService.findAllUsers()
        .then(res => res.json())
        .then(users => {
            users.forEach(user => {
                let tableFilling = "" +
                    "<div class='col-md-1 pt-2 pb-2'>" + user.id + "</div>" +
                    "<div class='col-md-2 pt-2 pb-2'>" + user.username + "</div>" +
                    "<div class='col-md-4 pt-2 pb-2'>" + user.email + "</div>" +
                    "<div class='col-md-2 pt-2 pb-2'>" + user.simpleRoles + "</div>" +

                    "<div class='col-md-1 pt-2 pb-2'><button type='button' class='btn btn-info' data-userid='" +
                    user.id + "' data-action='edit'  data-toggle='modal' data-target='#someDefaultModal'>Edit</button></div>" +

                    "<div class='col-md-2 pt-2 pb-2'><button type='button' class='btn btn-danger' data-userid='" +
                    user.id + "' data-action='delete' data-toggle='modal' data-target='#someDefaultModal'>Delete</button></div>";
                div.append(tableFilling);
            })
        })

    // обрабатываем нажатие на любую из кнопок edit или delete
    // достаем из нее данные и отдаем модалке, которую к тому же открываем
    $("#mainTableWithUsers").find('button').on('click', (event) => {
        let defaultModal = $('#someDefaultModal');

        let targetButton = $(event.target);

        let buttonUserId = targetButton.attr('data-userid');
        let buttonAction = targetButton.attr('data-action');

        defaultModal.attr('data-userid', buttonUserId);
        defaultModal.attr('data-action', buttonAction);
        defaultModal.modal('show');
    })
}


async function getNewUserForm() {
    let button = $(`#SliderNewUserForm`);
    let form = $(`#defaultSomeForm`)
    button.on('click', () => {
        if (form.attr("data-hidden") === "true") {
            form.attr('data-hidden', 'false');
            form.show();
            button.text('Hide panel');
        } else {
            form.attr('data-hidden', 'true');
            form.hide();
            button.text('Show panel');
        }
    })
}


// что то деалем при открытии модалки и при закрытии
// основываясь на ее дата атрибутах
async function getDefaultModal() {
    $('#someDefaultModal').modal({
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


// редактируем юзера из модалки редактирования, забираем данные, отправляем
async function editUser(modal, id) {
    let preuser = await userFetchService.findOneUser(id);
    let user = await preuser.json();
    let roleUser = user.roleUser;
    if (roleUser === "USER") {
        roleUser = "checked";
    } else {
        roleUser = "";
    }
    let roleAdmin = user.roleAdmin;
    if (roleAdmin === "ADMIN") {
        roleAdmin = "checked";
    } else {
        roleAdmin = "";
    }

    modal.find('.modal-title').html('Edit user');

    let editButton = `<button  class="btn btn-outline-success" id="editButton">Edit</button>`;
    let closeButton = `<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>`
    modal.find('.modal-footer').append(editButton);
    modal.find('.modal-footer').append(closeButton);

    //user.then(user => {
    let bodyForm = `<form class="form-group" id="editUser">
            <input type="text" class="form-control" id="id" name="id" value="${user.id}" disabled><br>
            <input class="form-control" type="text" id="username" value="${user.username}"><br>
            <input class="form-control" type="password" id="password"><br>
            <input class="form-control" id="email" type="email" value="${user.email}">
            <label for="roleUser">User</label> 
            <input type="checkbox" id="roleUser" ${roleUser}>&nbsp;&nbsp;
            <label for="roleAdmin">Admin</label> 
            <input type="checkbox" id="roleAdmin" ${roleAdmin}> 
        </form>`;
    modal.find('.modal-body').append(bodyForm);
    //})

    $("#editButton").on('click', async () => {
        let id = modal.find("#id").val().trim();
        let username = modal.find("#username").val().trim();
        let password = modal.find("#password").val().trim();
        let email = modal.find("#email").val().trim();
        let data = {
            id: id,
            username: username,
            password: password,
            email: email
        }
        const response = await userFetchService.updateUser(data, id);

        if (response.ok) {
            getTableWithUsers();
            modal.modal('hide');
        } else {
            let body = await response.json();
            let alert = `<div class="alert alert-danger alert-dismissible fade show col-12" role="alert" id="sharaBaraMessageError">
                            ${body.info}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
            modal.find('.modal-body').prepend(alert);
        }
    })
}


// удаляем юзера из модалки удаления
async function deleteUser(modal, id) {
    await userFetchService.deleteUser(id);
    getTableWithUsers();
    modal.find('.modal-title').html('Info');
    modal.find('.modal-body').html('User was deleted');
    let closeButton = `<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>`
    modal.find('.modal-footer').append(closeButton);
}


async function addNewUser() {
    $('#addNewUserButton').click(async () => {
        let addUserForm = $('#defaultSomeForm')
        let username = addUserForm.find('#AddNewUsername').val().trim();
        let password = addUserForm.find('#AddNewUserPassword').val().trim();
        let email = addUserForm.find('#AddNewEmail').val().trim();
        let data = {
            login: login,
            password: password,
            email: email
        }
        const response = await userFetchService.addNewUser(data);
        if (response.ok) {
            getTableWithUsers();
            addUserForm.find('#AddNewUsername').val('');
            addUserForm.find('#AddNewUserPassword').val('');
            addUserForm.find('#AddNewEmail').val('');
        } else {
            let body = await response.json();
            let alert = `<div class="alert alert-danger alert-dismissible fade show col-12" role="alert" id="sharaBaraMessageError">
                            ${body.info}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
            addUserForm.prepend(alert)
        }
    })
}