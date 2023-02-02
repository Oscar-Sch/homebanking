const { createApp } = Vue;

createApp({
    data() {
        return {
            data:null,
            transactions:[],
        }
    },
    created(){
        this.loadData();
    },
    methods:{
        loadData(){
            let id= new URLSearchParams(location.search).get("id");
            axios.get(`http://localhost:8080/api/accounts/${id}`)
            .then(res=>{
                this.data=res;
                this.transactions=res.data.transactions;
                // this.loadCharts();
            })
        },
        transactionClass(type){
            console.log(type)
            return `transaction-row ${type=="CREDIT"?"CREDIT":"DEBIT"}`
        },
    }
}).mount('#app')