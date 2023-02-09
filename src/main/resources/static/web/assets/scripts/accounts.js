const { createApp } = Vue;

createApp({
    data() {
        return {
            data:null,
            clientId:1,
            accounts:[],
            userAvatar:null,
            activeAccount:{},
        }
    },
    created(){
        this.loadData();
    },
    mounted(){
        this.asd();
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
            axios.get(`http://localhost:8080/api/clients/${this.clientId}`)
            .then(res=>{
                this.data=res;
                this.accounts=res.data.accounts;
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
        loadCharts(id){
            // console.log("asdasdasdasd")
            // console.log(id)
            let options = {
                chart: {
                  type: 'line'
                },
                series: [{
                  name: 'Balance',
                  data: [30,40,35,50,49,60,70,91,125,50,200,20]
                }],
                xaxis: {
                  categories: ["Ja","Fe","Ma","Ap","May","Jun","Jul","Au","Se","Oc","No","Di"]
                }
              }
            //   console.log(options)
              let dir=document.querySelector(`#${id}`);
              console.log(dir)
            //   if (dir){
                  var chart = new ApexCharts(dir, options);
                  chart.render();
            // //   }
            // console.log(dir)
              
        },
        openNav() {
            let container=document.querySelector(".lateral-navigation-subcontainer");
            if (!container.style.minWidth||container.style.minWidth=="3rem"){
                container.style.minWidth = "300px";
            }else{
                container.style.minWidth = "3rem";
            }
        },
        lastTransactionDate(transactions){
            let date= transactions.sort((a,b)=>b.id-a.id)[0].date.split("T");
            return `${date[0]} ${date[1].split(".")[0]}`
        }
    },
    computed:{
        hashUser(){
            return this.data.data.firstName[0]+this.data.data.lastName[0]+this.data.data.email;
        },
        
    }
}).mount('#app')