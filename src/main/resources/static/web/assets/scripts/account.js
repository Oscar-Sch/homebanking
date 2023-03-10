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
                this.transactions=res.data.transactions.sort((a,b)=>{
                    return b.id-a.id;
                });
                // this.loadCharts();
            })
        },
        transactionClass(type){
            console.log(type)
            return `transaction-row ${type=="CREDIT"?"CREDIT":"DEBIT"}`
        },
        openNav() {
            console.log("hola")
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
    }
}).mount('#app')