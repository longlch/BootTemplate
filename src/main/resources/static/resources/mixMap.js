function con_tho_an_co(callback) {
  setTimeout(function() {
    console.log('con thỏ ăn cỏ, uống nước');
    callback();
  }, 1000);
}
function hotel(count1) {
  console.log('chui vô hotel');
  for(let i=0;i<=1;i++){
  	console.log(count1[i]);
  }
}
var count=["1","2"];
con_tho_an_co(function(count){
	hotel(count);
});
