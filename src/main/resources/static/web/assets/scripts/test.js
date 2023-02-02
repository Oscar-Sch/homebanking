const hexagons=document.querySelectorAll(".hexagon");
hexagons.forEach(hex=>{
    hex.style.animationDelay= `${Math.random()*5}s`;
    console.log("first")
})