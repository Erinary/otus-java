var app = new Vue({
    el: '#app',
    data: {
        newUser: {
            name: "",
            age: 0,
            address: {
                street: ""
            },
            phones: [
                {number: ""}
            ],
        },
        users: []
    },
    methods: {
        loadData() {
            fetch("/users")
                .then(resp => {
                    return resp.json();
                })
                .then(json => {
                    this.users = json;
                });
        },
        saveUser() {
            fetch("/users", {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(this.newUser)
            })
                .then(resp => {
                    if (resp.status === 200) {
                        this.loadData();
                        alert("Created");
                    } else {
                        alert("Error " + resp.status);
                    }
                });
        }
    },
    beforeMount() {
        this.loadData();
    },
});