var app = new Vue({
    el: '#app',
    data: {
        newUser: {
            userName: "",
            userAge: 0,
            userAddress: "",
            userPhones: [
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
            fetch("users", {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(
                    {
                        userName: this.newUser.userName,
                        userAge: this.newUser.userAge,
                        userAddress: this.newUser.userAddress,
                        userPhones: this.newUser.userPhones.map(p => p.number)
                    }
                )
            })
                .then(resp => {
                    if (resp.status === 200) {
                        this.loadData();
                        alert("Created");
                    } else {
                        alert("Error " + resp.status);
                    }
                });
        },
        sendToWebSocket() {
            window.ws.send("Hello");
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