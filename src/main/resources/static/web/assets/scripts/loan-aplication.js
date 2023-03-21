const { createApp } = Vue;

createApp({
    data() {
        return {
            data:null,
            loans:[],
            activeLoan:null,
            activeAccount:null,
            accounts:[],
            amount:"",
            payPerIndex:"",
            loanErrMsg:"",
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
            axios.get(`/api/loans`)
            .then(res=>{
                this.loans=res.data;
            })
        },
        requestLoan(){
            axios.post("/api/loans",{
                "loanId":this.activeLoan.id,
                "amount":this.amount,
                "payments":this.activeLoan.payments[this.payPerIndex],
                "accountNumber":`${this.activeAccount}`
            })
            .then(res=>{
                window.location.assign("/web/accounts.html");
            })
            .catch(err=>{
                this.loanErrMsg=err.response.data;
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
        formatCurrency(amount){
            let options = { style: 'currency', currency: 'USD' };
            let numberFormat = new Intl.NumberFormat('en-US', options);
            return numberFormat.format(amount);
        },
        resetValues(){
            this.amount="";
            this.payPerIndex="";
            this.activeAccount=null;
        },
    },
    computed:{
        hashUser(){
            return this.data.data.firstName[0]+this.data.data.lastName[0]+this.data.data.email;
        },
        
    }
}).mount('#app')