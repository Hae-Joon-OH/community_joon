const btn = document.getElementById("registerBtn");
const nickNameCheck = document.getElementById("memberNickNameCheck");
const emailCheck = document.getElementById("memberEmailCheck");

// 등록 클릭시 발생하는 이벤트
btn.addEventListener('click', function () {


    if($("#memberName").val()=="") {
        alert("이름을 입력하세요.");
        $("#memberName").focus();
        return false;
    }
    if($("#memberNickname").val()=="") {
        alert("별명을 입력하세요.");
        $("#memberNickname").focus();
        return false;
    }
    if($("#memberNickNameCheck").attr('isChecked') == "no") {
        alert("별명 중복 확인을 진행하세요.");
        $("#memberNickNameCheck").focus();
        return false;
    }
    if($("#memberEmail").val()=="") {
        alert("이메일을 입력하세요.");
        $("#memberEmail").focus();
        return false;
    }
    if($("#memberEmailCheck").attr('isChecked') == "no") {
        alert("이메일 중복 확인을 진행하세요.");
        $("#memberEmailCheck").focus();
        return false;
    }
    if($("#memberPassword").val()=="") {
        alert("비밀번호를 입력하세요.");
        $("#memberPassword").focus();
        return false;
    }
    if($("#memberPasswordCheck").val() != $("#memberPassword").val()) {
        alert("비밀번호 확인을 진행하세요.");
        $("#memberPasswordCheck").focus();
        return false;
    }




    const Form = document.getElementById("form");

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('memberName', Form['memberName'].value);
    formData.append('memberPassword', Form['memberPassword'].value);
    formData.append('memberEmail', Form['memberEmail'].value);
    formData.append('memberNickname', Form['memberNickname'].value);

    xhr.open('post', '/member/registerProcess');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                alert("성공");
                window.location.href="/";
            } else {
                alert("실패");
            }
        }
    };
    xhr.send(formData);
});

// 별명 중복 확인
nickNameCheck.addEventListener('click', function(){
    const memberNickname = $("#memberNickname").val();
    if($("#memberNickname").val()=="") {
        alert("별명을 입력하세요.");
        $("#memberNickname").focus();
        return false;
    }
    $.ajax({
        url:"/member/nameDuplication",
        method:"post",
        data:{"memberNickname" : memberNickname},
        success: function(responseJson) {
            responseJson = JSON.parse(responseJson);
            if(responseJson['result'] == 'success') {
                alert("사용가능한 별명입니다.");
                $("#memberNickNameCheck").attr('isChecked', 'yes');
            } else {
                alert("중복된 별명입니다.")
            }
        }
    });
});

// Email 중복 확인
emailCheck.addEventListener('click', function(){
    const memberEmail = $("#memberEmail").val();
    $("#memberEmailCheck").attr('isChecked', 'yes');
    if($("#memberEmailCheck").val()=="") {
        alert("Email을 입력하세요.");
        $("#memberEmailCheck").focus();
        return false;
    }
    $.ajax({
        url:"/member/emailDuplication",
        method:"post",
        data:{"memberEmail" : memberEmail},
        success: function(responseJson) {
            responseJson = JSON.parse(responseJson);
            if(responseJson['result'] == 'success') {
                alert("사용가능한 Email입니다.");
                $("#memberEmailCheck").attr('isChecked', 'yes');
            } else {
                alert("중복된 Email입니다.")
            }
        }
    });

});

// 유효성 검사
