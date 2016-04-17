
/*(function() {
    alert("HERE");
    var url = location.href;
    alert(url);
    //var x = jsroutes.controllers.Application.enterUrlTest(url);
    $.ajax({
        url : "http://localhost:9000/EnterUrl?url="+url,
        crossDomain : true,
        success: function(data) {
            alert("Success");
            console.debug("Success of Ajax Call");
            console.debug(data);
        },
        error: function(err) {
            alert("An error occurred trying to find your location. Sorry :(");
            console.debug("Error of ajax Call");
            console.debug(err);
        }
    })
})();*/

javascript: (function(e, t) {
    var n = e.document;
    setTimeout(function() {
        function a(e) {
            if (e.data === "destroy_bookmarklet") {
                var r = n.getElementById(t);
                if (r) {
                    n.body.removeChild(r);
                    r = null
                }
            }
        }
        var t = "DELI_bookmarklet_iframe",
            r = n.getElementById(t);
        if (r) {
            return
        }
        var i = "http://localhost:9000/EnterUrl?",
            s = n.createElement("iframe");
        s.id = t;
        s.src = i + "url=" +
        encodeURIComponent(e.location.href) +
        "&title=" +
        encodeURIComponent(n.title)
        ;
        s.style.position = "fixed";
        s.style.top = "0";
        s.style.left = "0";
        s.style.height = "100%";
        s.style.width = "100%";
        s.style.zIndex = "16777270";
        s.style.border = "none";
        s.style.visibility = "hidden";
        s.onload = function() {
            this.style.visibility = "visible"
        };
        n.body.appendChild(s);
        var o = e.addEventListener ? "addEventListener" : "attachEvent";
        var u = o == "attachEvent" ? "onmessage" : "message";
        e[o](u, a, false)
    }, 1)
})(window)