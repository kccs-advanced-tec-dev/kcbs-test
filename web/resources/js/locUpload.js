// AccuratePosition
// 時間の許す限り正確な位置情報を取得する
// http://pear.jp/ 
function getAccuratePosition(successCallback, errorCallback, option){
    // 位置情報に対応していなければ終了
    if(!navigator.geolocation){
        var error     = new Object();
        error.code    = 9;
        error.message = 'GEOLOCATION_API_NOT_AVAILABLE';
        errorCallback(error);
        return;
    }
 
    // 変数の初期化
    var watch_id = undefined;
    var timer_id = undefined;
    var position = undefined;
    var limit    = option && option.limit   ? option.limit   : 100;
    var timeout  = option && option.timeout ? option.timeout : 0;
 
    // タイムアウトをセット
    if(timeout > 0){
        timer_id = setTimeout(
            function(){
                // 位置情報の取得を中止する
                if(watch_id){
                    navigator.geolocation.clearWatch(watch_id);
                    watch_id = undefined;
                }
 
                // 位置情報が取得できていればsuccessCallbackに送る
                if(position){
                    successCallback(position);
                }
                else{
                    var error     = new Object();
                    error.code    = 9;
                    error.message = 'TIMEOUT';
                    errorCallback(error);
                    return;
                }
            },
            timeout
        );
    }
 
    // 取得を実行
    watch_id = navigator.geolocation.watchPosition(
        function(p){
            // 取得のたびに更新する
            position = p;
 
            // 求める精度に達すればsuccessCallbackに送る
            if(position.coords.accuracy < limit){
                // タイムアウト監視を止める
                if(timer_id){
                    clearTimeout(timer_id);
                    timer_id = undefined;
                }
                navigator.geolocation.clearWatch(watch_id);
                successCallback(position);
            }
        },
        function(e){
            errorCallback(e);
        },
        {enableHighAccuracy:true, maximumAge:0}
    );
}

function dispMessage(msg) {
    alert(msg);
}

function getLocation(locationUrl) {
    getAccuratePosition(
        function(pos) {
            g_location = 'N' + pos.coords.latitude + 'E' + pos.coords.longitude;
        },
        function(error){
            g_location = error.message;
        },
        {limit:100, timeout:4000}
    );
    
    url = g_locationUrl;
    url += '/';
    url += g_macAddr;
    url += '/';
    url += g_prgname;
    url += '/';
    url += g_location;
    $.ajax({
        type: 'GET',
        url: url,
        dataType: "text",
        success: function(data){},
        error: function(jqXHR, textStatus, errorThrown) {
            //dispMessage('getLocation failed');
        }
    });
    
    return g_location;
}

// localStorageからのデータの取得と表示
function viewStorage() {
    for (var i = 0; i < localStorage.length; i++) {
        key = localStorage.key(i);
        val = localStorage.getItem(key);
        alert(key + ':' + val);
    }
}

// localStorageからすべて削除
function removeallStorage() {
  localStorage.clear();
}
