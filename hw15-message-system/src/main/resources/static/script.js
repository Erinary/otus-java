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
        }
    },
    beforeMount() {
        window.ws = new WebSocket(`ws://${window.location.host}${window.location.pathname}/ws`);
        //TODO обработка ответов от бэка
        window.ws.onmessage = (msg) => {
            console.info("Message from WebSocket: ", msg.data)
        }
    },
});