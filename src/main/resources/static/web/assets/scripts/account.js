const { createApp } = Vue;

createApp({
    data() {
        return {
            data:null,
            account:null,
            transactions:[],
            fromDate:"",
            thruDate:"",
            coins:[],
            accountCoins:[],
            accountCoinsGeneric:[],
            filteredCoins:[],
        }
    },
    created(){
        this.loadData();
    },
    methods:{
        loadData(){
            let id= new URLSearchParams(location.search).get("id");
            axios.get(`/api/clients/current`)
            .then(res=>{
                this.data=res;
                this.account=this.data.data.accounts.find(acc=> acc.id==id);
                this.transactions=this.account.transactions.sort((a,b)=>{
                    if(a.date>=b.date){
                        return -1;
                    }else{
                        return 1;
                    }
                });
                if(this.account.type==="CRYPTO"){
                    axios.get("https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false")
                    .then(res=>{
                        this.coins=res.data;
                        this.accountCoins=this.account.coins;
                        this.accountCoinsGeneric=this.coins.filter(coin=>{
                            return this.account.coins.some(co=> co.coinId===coin.id);
                        })
                        this.filteredCoins=this.accountCoinsGeneric;
                    })
                }
            })
        },
        transactionClass(type){
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
        formatCurrency(amount){
            let options = { style: 'currency', currency: 'USD' };
            let numberFormat = new Intl.NumberFormat('en-US', options);
            return numberFormat.format(amount);
        },
        generatePDF(){
            const elem=document.querySelector(".print-content");
            let opt = {
                margin:[15,0],
                filename:`Invoice-${("0000"+(Math.random()*9999)).slice(-4)}.pdf`,
            };
            html2pdf().set(opt).from(elem).save();
            
        },
        dateFilter(){
            let fDate= this.fromDate?new Date(this.fromDate).toISOString():"";
            let tDate= this.thruDate?new Date(this.thruDate).toISOString():"";
            axios.get(`/api/clients/current/transactions?accountNumber=${this.account.number}&fromDate=${fDate}&thruDate=${tDate}`)
            .then(res=>{
                this.transactions=res.data.sort((a,b)=>{
                    if(a.date>=b.date){
                        return -1;
                    }else{
                        return 1;
                    }
                });
            })
        },
    },
    computed:{
        hashUser(){
            return this.data.data.firstName[0]+this.data.data.lastName[0]+this.data.data.email;
        },
        // datete(){
        //     return new Date(this.date).toISOString();
        // }
    }
}).mount('#app')