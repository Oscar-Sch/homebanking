const { createApp } = Vue;

createApp({
    data() {
        return {
            data:null,
            clientId:1,
            accounts:[],
            loans:[],
            userAvatar:null,
            activeAccount:{},
        }
    },
    created(){
        this.loadData();
    },
    mounted(){
        // this.asd();
        // console.log(this.accounts)
    },
    updated(){
        // this.accNumber();
        // this.loadCharts("chart0");
    },
    methods:{
        asd(){
            this.accounts.forEach((element,ind) => {
                this.loadCharts(`chart${ind}`)  
                console.log(ind)  
            });
        },
        loadData(){
            axios.get(`http://localhost:8080/api/clients/current`)
            .then(res=>{
                this.data=res;
                this.accounts=res.data.accounts;
                this.loans=res.data.loans;
                this.openCarousel();
                // this.loadCharts();
            })
        },
        parseDate(date){
            return date.split("T")[0];

        },
        accNumber(){
            let number=document.querySelector(".account-number");
            // console.log(number)
        },
        activeData(account){
            // console.log(account)
            this.activeAccount.number=account.number;
            this.activeAccount.date=account.currentDate;
        },
        // loadCharts(id){
        //     // console.log("asdasdasdasd")
        //     // console.log(id)
        //     let options = {
        //         chart: {
        //           type: 'line'
        //         },
        //         series: [{
        //           name: 'Balance',
        //           data: [30,40,35,50,49,60,70,91,125,50,200,20]
        //         }],
        //         xaxis: {
        //           categories: ["Ja","Fe","Ma","Ap","May","Jun","Jul","Au","Se","Oc","No","Di"]
        //         }
        //       }
        //     //   console.log(options)
        //       let dir=document.querySelector(`#${id}`);
        //       console.log(dir)
        //     //   if (dir){
        //           var chart = new ApexCharts(dir, options);
        //           chart.render();
        //     // //   }
        //     // console.log(dir)
              
        // },
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
        openCarousel(){
            const root=document.querySelector(":root");
            setTimeout(() => {
                const display= document.querySelectorAll(".section-container");
                root.style.setProperty("--animation-section-open","section-open 3s forwards");
                root.style.setProperty("--animation-section-open-glitch","section-open-glitch 3s forwards");
                root.style.setProperty("--animation-section-open-glitch-shadow","section-open-glitch-shadow 3s forwards");
                root.style.setProperty("--animation-hex-start","hex-start 1s forwards");
                root.style.setProperty("--animation-hex-shadow-start","hex-shadow-start 1s forwards");
                root.style.setProperty("--animation-bot-shadow","bot-shadow 1s forwards");
                root.style.setProperty("--animation-bot-start-left","bot-start-left .5s forwards");
                root.style.setProperty("--animation-bot-start-right","bot-start-right .5s forwards");
                setTimeout(() => {
                    root.style.setProperty("--animation-section-open","none");
                    root.style.setProperty("--animation-section-open-glitch","none");
                    root.style.setProperty("--animation-section-open-glitch-shadow","none");
                    root.style.setProperty("--display-background",`0 0 1rem rgb(190, 246, 246),
                    0 0 2rem cyan, 0px 15px 10px -10px white inset
                    , 0 0 2rem cyan, 0px -15px 10px -10px white inset`);
                    display.forEach(elem=>{
                        elem.style.maxHeight="100vh";
                        elem.style.minHeight="25rem";
                        elem.style.opacity="1";
                    })
                }, 2200);
            }, 100);
        },
        lastTransactionDate(transactions){
            let date= transactions.sort((a,b)=>b.id-a.id)[0].date.split("T");
            return `${date[0]} ${date[1].split(".")[0]}`
        },
        transactionClass(type){
            console.log(type)
            return `transaction-row ${type=="CREDIT"?"CREDIT":"DEBIT"}`
        },
        formatCurrency(amount){
            let options = { style: 'currency', currency: 'USD' };
            let numberFormat = new Intl.NumberFormat('en-US', options);
            return numberFormat.format(amount);
        },
        sesionLogout(){
            axios.post('/api/logout').then(response => {
                console.log('signed out!!!')
                window.location.href = '/web/index.html';
            })
            
        },
        createAccount(){
            axios.post("/api/clients/current/accounts")
            .then(res=>{
                window.location.reload()
            })
            .catch(err=> console.error(err.message))
        }
    },
    computed:{
        hashUser(){
            return this.data.data.firstName[0]+this.data.data.lastName[0]+this.data.data.email;
        },
    }
}).mount('#app')