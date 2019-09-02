var app = new Vue({
    el: '#app',
    data: {
        newUser: {
            name: "",
            age: 0,
            address: "",
            phones: [
                {number: ""}
            ],
        },
        users: []
    },
    methods: {
        loadData() {
            window.ws.send(
                JSON.stringify(
                    {
                        type: "loadUsersRequest",
                    }
                )
            )
        },
        saveUser() {
            window.ws.send(
                JSON.stringify(
                    {
                        type: "createUserRequest",
                        data: {
                            name: this.newUser.name,
                            age: this.newUser.age,
                            address: this.newUser.address,
                            phones: this.newUser.phones.map(p => p.number)
                        }
                    }
                )
            );
        },
        storeLoadedUsers(response) {
            this.users = response.users;
        }
    },
    beforeMount() {
        window.ws = new WebSocket(`ws://${window.location.host}${window.location.pathname}/ws`);
        window.ws.onmessage = (msg) => {
            console.info("Message from WebSocket: ", msg.data);
            const response = JSON.parse(msg.data);
            if (response.type === "loadUsersResponse") {
                this.storeLoadedUsers(response);
            } else {
                alert(response.message)
            }
        }
    },
});