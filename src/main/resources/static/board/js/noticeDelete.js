$("#dBtn").click(function(){
   const boardNum = $("#boardNum").val();
   if(confirm('정말 삭제하시겠습니까?')) {
       window.location.href = "/board/noticeDelete?boardNum=" + boardNum;
   } else {
       window.location.href = "/board/noticeDetail?boardNum=" + boardNum;
   }
});