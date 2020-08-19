/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//nav bar links fixed on click
$(function(){
    $('.aNav').click(function(){
        $('.aNav').removeClass('active');
        $(this).addClass('active');
    });
});

$(document).ready(function(){
  
  /*Hide some col or adjust their spacing, when resizing*/
  if(screen.width < 768 || $(window).width()<768){
      $('.col-priority-low').addClass('d-none');
      $('.col-6-12').addClass('col-12');
  }
  
  $(window).resize(function(){
      console.log("Resize " + $(window).width());
      if($(window).width()<768){
        $('.col-priority-low').addClass('d-none');
        $('.col-6-12').addClass('col-12');
        $('.col-6-12').removeClass('col-6');
      }else{
        $('.col-priority-low').removeClass('d-none');
         $('.col-6-12').addClass('col-6');
        $('.col-6-12').removeClass('col-12');
      }
  });
});
