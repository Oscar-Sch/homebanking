const { createApp } = Vue;

createApp({
    data() {
        return {
            data:null,
            accounts:[],
            targetOwner:"no",
            originAccount:null,
            targetAccount:null,
            targetAccountFetched:null,
            destinationNumber:null,
            amount:null,
            description:null,
            registerErrMsg:"",
        }
    },
    created(){
        this.loadData();
    },
    methods:{
        loadData(){
            axios.get(`/api/clients/current`)
            .then(res=>{
                this.data=res;
                this.accounts=res.data.accounts;
            })
        },
        openNav() {
            let container=document.querySelector(".lateral-navigation-subcontainer");
            if (window.innerWidth>768){
                if (!container.style.minWidth||container.style.minWidth=="3rem"){
                    container.style.minWidth = "300px";
                }else{
                    container.style.minWidth = "3rem";
                }
            }else{
                if (!container.style.minHeight||container.style.minHeight=="3rem"){
                    container.style.minHeight = "300px";
                }else{
                    container.style.minHeight = "3rem";
                }
            }
        },
        sesionLogout(){
            axios.post('/api/logout').then(response => {
                console.log('signed out!!!')
                window.location.href = '/web/index.html';
            })
            
        },
        transfer(){
            this.registerErrMsg="";
            axios.post('/api/transactions',`amount=${this.amount}&originAccountNumber=${this.originAccount.number}&targetAccountNumber=${this.targetAccount?.number || this.destinationNumber}&description=${this.description}`)
            .then(response => {
                window.location.reload();
            })
            .catch(err=>{
                this.registerErrMsg=err.response.data;
            })
        },
        formatCurrency(amount){
            let options = { style: 'currency', currency: 'USD' };
            let numberFormat = new Intl.NumberFormat('en-US', options);
            return numberFormat.format(amount);
        },
        resetData(){
            this.targetAccount=null;
            this.destinationNumber=null;
            this.targetAccountFetched=null;
        },
        fetchTargetAccount(){
            if (this.destinationNumber && this.destinationNumber.length===12){
                axios.get(`/api/accounts/target?number=${this.destinationNumber}`)
                .then(res=>{
                    this.targetAccountFetched=res.data;
                })
            }
        },
    },
    computed:{
        hashUser(){
            return this.data.data.firstName[0]+this.data.data.lastName[0]+this.data.data.email;
        },
        
    }
}).mount('#app')