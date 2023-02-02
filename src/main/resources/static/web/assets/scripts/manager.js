const { createApp } = Vue

createApp({
    data() {
        return {
            data:null,
            clientList:null,
            clientListFiltered:null,
            newClientData:{firstName:"",lastName:"",email:"",id:""},
            clientToEdit:{firstName:"",lastName:"",email:""},
            isEditing:false,
            searchValue:""
        }
    },
    created(){
        this.loadData();
    },
    methods:{
        loadData(){
            axios.get("http://localhost:8080/api/clients")
            .then(res=>{
                this.data=res;
                this.clientList=res.data;
                this.clientListFiltered=this.clientList;
            })
        },
        postClient(){
            axios.post("http://localhost:8080/api/clients",
            {firstName:this.newClientData.firstName,
            lastName:this.newClientData.lastName,
            email:this.newClientData.email})
            .then(res=>{
                this.loadData();
                this.clearFormFields();
            })
            .catch(res=>{
                alert("no vamo nada")
            })
        },
        addClient(){
            if (this.newClientData.firstName!==""&&this.newClientData.lastName!==""&&this.newClientData.email!==""){
                this.postClient();
            }else{
                alert("You have to fill all the fields!");
            }
        },
        editClient(){
            if (this.newClientData.firstName!==""&&this.newClientData.lastName!==""&&this.newClientData.email!==""){
                axios.patch(`http://localhost:8080/api/clients/${this.newClientData.id}`,
                    {firstName:this.newClientData.firstName,
                    lastName:this.newClientData.lastName,
                    email:this.newClientData.email})
                .then(res=>{
                    console.log("se pudo")
                    this.stopEditing();
                    this.loadData();
                })
            }else{
                alert("You have to fill all the fields!");
            }
        },
        deleteClient(){
            axios.delete(`http://localhost:8080/api/clients/${this.newClientData.id}`)
            .then(res=>{
                this.stopEditing();
                this.loadData();
            })
        },
        stopEditing(){
            this.isEditing=false;
            this.clearFormFields();
            this.searchValue="";
        },
        clearFormFields(){
            this.newClientData.firstName="";
            this.newClientData.lastName="";
            this.newClientData.email="";
        },
        startEditing(client){
            this.newClientData.firstName=client.firstName;
            this.newClientData.lastName=client.lastName;
            this.newClientData.email=client.email;
            this.newClientData.id=client.id;
            this.isEditing=true;
            window.scrollTo(0,0);
        },
        filterTable(){
            this.clientListFiltered=this.clientList.filter(client=>
                client.firstName.toLowerCase().includes(this.searchValue.toLowerCase()) ||
                client.lastName.toLowerCase().includes(this.searchValue.toLowerCase()) ||
                client.email.toLowerCase().includes(this.searchValue.toLowerCase())
            )
        }
    },

}).mount('#app')