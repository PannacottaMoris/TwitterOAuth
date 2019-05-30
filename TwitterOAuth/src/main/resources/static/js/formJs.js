$(function(){
	//var target = "http://ec2-3-112-189-8.ap-northeast-1.compute.amazonaws.com:9085/";
	var target = "http://localhost:9085/";
	$('#errText').css({ color:"red" });
	$('#addErrText').css({ color:"red" });
	var errorCode = {
		'noError'					:0x00000000,
		'maxPriceError'				:0x00000001,
		'minPriceError'				:0x00000010,
		'searchNameError'			:0x00000100,
		'searchDescriptionError'	:0x00001000,
		'nameError'					:0x00010000,
		'descriptionError'			:0x00100000,
		'priceError'				:0x01000000,
	};
	var mask = {
		'search':0x00001111,
		'add'	:0x01110000,
	};
	var errorBit = errorCode['noError'];

	$("#response").html("Response Values");

	//フォーム入力値をチェックする
	function checkPattern (target, errField, pattern) {
		if (target.match(pattern) || target == ""){
			errorBit &= ~(errorCode[errField]);
		} else {
			errorBit |= errorCode[errField];
		}
	}

	//空白検知
	//searchであれば全て空白の場合はtrue呼びもとでエラー処理を行う
	//addであれば空白項目が1つでもあればtrue
	//ただし、deleteの場合は名前フィールドのみ空白確認を行う
	function isAllBlank (mode) {
		var deleteFlg = false;
		if ($('input[name="other"]:checked').val() == "delete"){
			deleteFlg = true;
		}

		if(mode == "search"){
			if ($("#name_s").val() != ""
				|| $("#description_s").val() != ""
				|| $("#maxPrice_s").val() != ""
				|| $("#minPrice_s").val() != ""){
				return false;
			} else {
				return true;
			}
		} else if (mode == "add" && deleteFlg){
			if ($("#name").val() != "") {
				return false;
			} else {
				return true;
			}
		} else if (mode = "add") {
			if ($("#name").val() != ""
				&& $("#description").val() != ""
				&& $("#price").val() != "") {
				return false
			} else {
				return true;
			}
		}
		return true;
	}

	//エラーの発生を確認する
	function isInvalid (mode) {
		if (errorBit & mask[mode]) {
			return true;
		} else {
			return false;
		}
	}

	//エラー発生しているフォームの背景色を変更する
	function formColor(id, errField){
		if (errorBit & errorCode[errField]){
			$(id).css({ background:"#F7D8D9" });
		}else{
			$(id).css({ background:"transparent" });
		}
	}

	//エラーメッセージを表示する
	function errText (msg, errField, mode){
		var id;
		if (mode == "search"){
			id = "#errText";
		}else if(mode == "add"){
			id = "#addErrText";
		}

		if (errorBit & errorCode[errField]){
			$(id).text("  " + msg);
		}else{
			$(id).text("");
		}
	}

	//検索商品名検証
	$('#name_s').keyup(function() {
		var pattern = new RegExp("^[a-zA-Z]*$");
		checkPattern($("#name_s").val(), "searchNameError", pattern);
		formColor('#name_s', "searchNameError");
		errText ("半角英数のみ入力できます", "searchNameError", "search");
	});
	//検索説明検証
	$('#description_s').keyup(function() {
		var pattern = new RegExp("^[a-zA-Z0-9][a-zA-Z ,\.]*$");
		checkPattern($("#description_s").val(), "searchDescriptionError", pattern);
		formColor('#description_s', "searchDescriptionError");
		errText ("半角英数と一部記号のみ入力できます", "searchDescriptionError", "search");
	});
	//検索最高値検証
	$('#maxPrice_s').keyup(function() {
		var pattern = new RegExp("^[1-9][0-9]*$");
		checkPattern($("#maxPrice_s").val(), "maxPriceError", pattern);
		formColor('#maxPrice_s', "maxPriceError");
		errText ("不適切な入力値があります", "maxPriceError", "search");
	});
	//検索最安値検証
	$('#minPrice_s').keyup(function() {
		var pattern = new RegExp("^[1-9][0-9]*$");
		checkPattern($("#minPrice_s").val(), "minPriceError", pattern);
		formColor('#minPrice_s', "minPriceError");
		errText ("不適切な入力値があります", "minPriceError", "search");
	});

	//検索
	$("#button_search").click( function(){
		var url = target + "search";

		if (isInvalid("search") == true || isAllBlank("search") == true){
			$('#errText').text("   入力欄に誤りがあります");
			return false;
		}

		var JSONdata = {
			name: $("#name_s").val(),
			description: $("#description_s").val(),
			maxPrice: $("#maxPrice_s").val(),
			minPrice: $("#minPrice_s").val()
		};
		alert(JSON.stringify(JSONdata));

		$.ajax({
			type : 'post',
			url : url,
			data : JSON.stringify(JSONdata),
			contentType: 'application/JSON',
			dataType : 'JSON',
			scriptCharset: 'utf-8',
			success : function(data) {
				alert(JSON.stringify(data));
				$("#response").html(JSON.stringify(data));
			},
			error : function(data) {
				alert("error");
				alert(JSON.stringify(data));
				$("#response").html(JSON.stringify(data));
			}
		});
	})

	//追加等商品名検証
	$('#name').keyup(function() {
		var pattern = new RegExp("^[a-zA-Z]*$");
		checkPattern($("#name").val(), "nameError", pattern);
		formColor('#name', "nameError");
		errText ("半角英数のみ入力できます", "nameError", "add");
	});
	//追加等説明検証
	$('#description').keyup(function() {
		var pattern = new RegExp("^[a-zA-Z \.,]*$");
		checkPattern($("#description").val(), "descriptionError", pattern);
		formColor('#description', "descriptionError");
		errText ("半角英数のみ入力できます", "descriptionError", "add");
	});
	//追加等値段検証
	$('#price').keyup(function() {
		var pattern = new RegExp("^[1-9][0-9]*$");
		checkPattern($("#price").val(), "priceError", pattern);
		formColor('#price', "priceError");
		errText ("半角英数のみ入力できます", "priceError", "add");
	});

	//追加・削除・更新
	$("#button_other").click( function(){
		var url = target + $('input[name="other"]:checked').val();
		if($('input[name="other"]:checked').val() == null){
			$('#addErrText').text("   入力欄に誤りがあります");
			return false;
		}
		else if (isInvalid("add") == true || isAllBlank("add") == true){
			$('#addErrText').text("   入力欄に誤りがあります");
			return false;
		}

		var JSONdata = {
			name: $("#name").val(),
			description: $("#description").val(),
			price: $("#price").val()
		};
		alert("Send to: " + url + "    Body: " + JSON.stringify(JSONdata));

		$.ajax({
			type : 'post',
			url : url,
			data : JSON.stringify(JSONdata),
			contentType: 'application/JSON',
			dataType : 'JSON',
			scriptCharset: 'utf-8',
			success : function(data) {
				alert(JSON.stringify(data));
				$("#response").html(JSON.stringify(data));
			},
			error : function(data) {
				alert("error");
				alert(JSON.stringify(data));
				$("#response").html(JSON.stringify(data));
			}
		});
	})
})