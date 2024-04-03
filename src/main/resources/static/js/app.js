$(async function () {
    await getTableWithUsers();
    getNewUserForm();
    getDefaultModal();
    getNewUserForm();
})


const userFetchService = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Referer': null
    },
    findAllUsers: async () => await fetch('/api/users'),
    findOneUser: async (id) => await fetch('api/users/' + id),
    addNewUser: async (user) => await fetch('/api/users', {
        method: 'POST',
        headers: userFetchService.head,
        body: JSON.stringify(user)
    }),
    updateUser: async (user, id) => await fetch(`/api/users/${id}`, {
        method: 'PUT',
        headers: userFetchService.head,
        body: JSON.stringify(user, id)
    }),
    deleteUser: async (id) => await fetch(`/api/users/${id}`, {method: 'DELETE', headers: userFetchService.head})
}

async function getTableWithUsers() {
    let div = $('#mainTableWithUsers');
    div.empty();
    $('#mainContent h4').empty().append("All users");
    $('#buttonNewUser').empty().append("<a href='javascript:void(0);'>New user</a>").removeClass("bg-white border");
    $('#buttonUserTable').empty().append("User table").addClass("bg-white border border-bottom-0");
    $('#mainTableWithUsers').addClass("border-top").removeClass("text-center");
    let headTable = `
        <div class="col-md-1 pt-2 pb-2"><strong>ID</strong></div>
        <div class="col-md-2 pt-2 pb-2"><strong>Username</strong></div>
        <div class="col-md-3 pt-2 pb-2"><strong>Email</strong></div>
        <div class="col-md-2 pt-2 pb-2"><strong>Role</strong></div>
        <div class="col-md-2 pt-2 pb-2"><strong>Edit</strong></div>
        <div class="col-md-2 pt-2 pb-2"><strong>Delete</strong></div> 
    `;
    $('#headTable').empty().append(headTable);
    await userFetchService.findAllUsers()
        .then(res => res.json())
        .then(users => {
            users.forEach(user => {
                let tableFilling = "" +
                    "<div class='col-md-1 pt-2 pb-2'>" + user.id + "</div>" +
                    "<div class='col-md-2 pt-2 pb-2 text-truncate'>" + user.username + "</div>" +
                    "<div class='col-md-3 pt-2 pb-2 text-truncate'>" + user.email + "</div>" +
                    "<div class='col-md-2 pt-2 pb-2'>" + user.simpleRoles + "</div>" +
                    "<div class='col-md-2 pt-2 pb-2'><button type='button' class='btn btn-info' data-userid='" +
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

async function setRole(role) {
    if ($('#roleUser').prop('checked')) {
        let roleUser = {
            id: 1,
            name: "ROLE_USER",
        };
        role.push(roleUser)
    }

    if ($('#roleAdmin').prop('checked')) {
        let roleAdmin = {
            id: 2,
            name: "ROLE_ADMIN",
        };
        role.push(roleAdmin)
    }
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

    let editButton = `<button  class="btn btn-outline-success bg-primary text-white" id="editButton">Edit</button>`;
    let closeButton = `<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>`
    modal.find('.modal-footer').append(closeButton);
    modal.find('.modal-footer').append(editButton);

    //user.then(user => {
    let bodyForm = `<form class="form-group mb-0" id="editUser">
            <input type="hidden" class="form-control bg-warning" id="id" name="id" value="${user.id}" disabled><br>
            <input class="form-control bg-warning" type="text" id="username" value="${user.username}"><br>
            <input class="form-control bg-warning" type="password" id="password"><br>
            <input class="form-control bg-warning" id="email" type="email" value="${user.email}">
            <div class="pt-4 text-center">
                <label for="roleUser">User</label> 
                <input type="checkbox" id="roleUser" ${roleUser}>&nbsp;&nbsp;
                <label for="roleAdmin">Admin</label> 
                <input type="checkbox" id="roleAdmin" ${roleAdmin}> 
            </div>    
        </form>`;
    modal.find('.modal-body').append(bodyForm);
    //})

    $("#editButton").on('click', async () => {
        let id = modal.find("#id").val().trim();
        let username = modal.find("#username").val().trim();
        let password = modal.find("#password").val().trim();
        let email = modal.find("#email").val().trim();

        let role = [];

        setRole(role);

        let data = {
            id: id,
            username: username,
            password: password,
            email: email,
            roles: role
        }
        const response = await userFetchService.updateUser(data, id);

        if (response.ok) {
            getTableWithUsers();
            modal.modal('hide');
        } else if (response.status === 400) {
            alert("Для сохранения заполните все обязательные поля");
        } else if (response.status === 409) {
            alert("Имя пользователя занято");
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

async function getNewUserForm() {
    $('#buttonNewUser').click(async () => {
        let div = $('#mainTableWithUsers');
        let tableNewUser = `
            <form class="w-100" id="newUserForm">
                <div class="col-md-12 pt-3 pb-2">
                    <label for="username"><b>Username</b></label><br/>
                    <input class="bg-warning" type="text" id="username" name="username" required/>
                </div>
                <div class="col-md-12 pt-2 pb-2">
                    <label for="password"><b>Password</b></label><br/>
                    <input class="bg-warning" type="password" id="password" name="password" required/>
                </div>
                <div class="col-md-12 pt-2 pb-2">
                    <label for="email"><b>E-mail</b></label><br/>
                    <input class="bg-warning" type="email" id="email" name="email" required/>
                </div>
                <div class="col-md-12 pt-2 pb-2">
                    <label><b>Role</b></label><br/>
                    <label for="roleUser">user</label>
                    <input type="checkbox" id="roleUser" name="roleUser">
                    <label for="roleAdmin">admin</label>
                    <input type="checkbox" id="roleAdmin" name="roleAdmin">
                </div>
            </form>   
            <div class="col-md-12 pt-2 pb-3">
                <button type="button" class="btn btn-success" id ="buttonAddNewUser">Add new user</button>
            </div>                    
        `;
        $('#mainContent h4').empty().append("New user");
        $('#headTable').empty().removeClass("border-top");
        $('#buttonUserTable').empty().append("<a href='javascript:void(0);'>User table</a>").removeClass("bg-white border");
        $('#buttonNewUser').empty().append("New user").addClass("bg-white border border-bottom-0");
        $('#mainTableWithUsers').removeClass("border-top").addClass("text-center");

        div.empty();
        div.append(tableNewUser);

        $("#buttonAddNewUser").on('click', async () => {
            let newUserForm = "newUserForm";
            let username = $('#newUserForm').find("#username").val().trim();
            let password = $('#newUserForm').find("#password").val().trim();
            let email = $('#newUserForm').find("#email").val().trim();
            let role = [];

            setRole(role);

            let user = {
                username: username,
                password: password,
                email: email,
                roles: role
            }
            const response = await userFetchService.addNewUser(user);
            if (response.ok) {
                getTableWithUsers();
            } else if (response.status === 409) {
                alert("Такой пользователь уже существует");
            } else if (response.status === 400) {
                alert("Заполните все обязательные поля");
            }
        })
    })
}
$('#buttonUserTable').click(async () => {
    getTableWithUsers();
})