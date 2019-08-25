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
            fetch("users")
                .then(resp => {
                    return resp.json();
                })
                .then(json => {
                    this.users = json;
                });
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
        // this.loadData();
        window.ws = new WebSocket(`ws://${window.location.host}${window.location.pathname}/ws`);
        window.ws.onmessage = (msg) => {
            console.info("Message from WebSocket: ", msg.data)
        }
    },
});