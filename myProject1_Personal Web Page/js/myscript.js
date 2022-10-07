//get ul element and add same set of photos in order to make them adjacent to each other
let ul=document.querySelector('ul');
ul.innerHTML = ul.innerHTML+ul.innerHTML;
//get all ul elements and .btn elements
let lis=document.querySelectorAll('li');
let btns=document.querySelectorAll('.btn');
//positive number: roll to right, negative number: roll to left
let spa=-2;
//calculate and set the total width of ul
ul.style.width=lis[0].offsetWidth*lis.length+'px';

//use move() function
function move(){
    // console.log(ul.offsetLeft)
    if(ul.offsetLeft < -ul.offsetWidth/2) {
        //check whether photos are used up while going left
        //if so, start all over again
        ul.style.left='0';
    }    
    if (ul.offsetLeft > 0) {
        //check whether photos are used up while going right
        ul.style.left=-ul.offsetWidth/2+'px';
    }
    //set offset position
    ul.style.left=ul.offsetLeft+spa+'px';
}

//run move() function every 30 millisecond
let timer=setInterval(move, 30);

//stops when hovers, scrolling when moves away
ul.addEventListener('mousemove', function() {
    clearInterval(timer);
})
ul.addEventListener('mouseout', function() {
    timer=setInterval(move, 30);
})
//go left button get click event
btns[0].addEventListener('click', function(){
    spa=-2;
})
//go right button get click event
btns[1].addEventListener('click', function(){
    spa=2;
})