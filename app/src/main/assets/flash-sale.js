$s = jQuery.noConflict();
var xx = window.location.href;
n5sd = "6 1 2018 12:00:00";
mitvd= "5 31 2018 12:00:00";
var ti = 0, apic = 0;
var sfvhj='';
fkautobuy("min5pb", "Redmi Note 5 pro Black 4GB", n5sd, "pid=MOBF28FTQPHUPX83", 5, sfvhj, 'LSTMOBF28FTQPHUPX83H7IIOZ');

function getnextdate(sd) {
    var cdate = new Date()
        .getTime();
    while (cdate > sd) sd = sd + 7 * 24 * 60 * 60000;
    return sd;
}
function fkautobuy(cookie, mobile, date, stri, refresh, msg1, emid) {
    //console.log(date);
    date = new Date(date)
        .getTime();
    if (xx.search(stri) > 0) {
        if (msg1) msg = msg1;
        else msg = "";
        eid = emid;
        FK3buy(date, mobile, refresh);
    }
}

function FK3buy(mobdate, mobname, refresh) {
    var ele = document.getElementById("fyureka");
    if (!ele) {
        var elemDiv = document.createElement('div');
        elemDiv.id = "fyureka";
        elemDiv.style.cssText = 'width: 100%; position: fixed; bottom: 40px; right: 0px; z-index: 99999; border: green; border-radius: 10px;box-shadow: 4px 4px 20px green;background: aliceblue;';
        document.body.appendChild(elemDiv);
        document.getElementById("fyureka")
            .innerHTML = '<div id="fyureka" style="width: 100%;position: fixed;bottom: 50px;right: 0px;z-index: 99999;/* border: green; */border-radius: 10px;box-shadow: green 4px 4px 20px;background: aliceblue;"><div><p id="fmsg" style="display: table-cell;vertical-align: middle;padding: 10px 20px;font-family: Helvetica, Arial,sans-serif;font-size: 12px;color: #c60;margin: 0;font-weight: 600;line-height: 16px;"></p></div></div>';
        var ele = document.getElementById("fyureka");
    }
    cdate = new Date()
        .getTime();
    var tymleft = getnextdate(mobdate) - cdate;
    if (tymleft > 60 * 60000 && tymleft < 601200000) {
        document.getElementById("fmsg")
            .innerHTML = "Login to your account if not already";
        setTimeout(function() {
            FK3buy(mobdate, mobname, refresh)
        }, tymleft - 59 * 60000);
    } else if (tymleft < 3600000 && tymleft > 240000) { // var timeleft = document.getElementsByClassName("timeleft-large");
        ele.style.background = "gold";
        document.getElementById("fmsg")
            .innerHTML = "Don't forget to login before sale.<br>Click refresh if I do not turn green before 3 minutes of sale";
        setTimeout(function() {
            FK3buy(mobdate, mobname, refresh)
        }, tymleft - 239000);
    } else if (tymleft < 240000 && tymleft > 180000) {
        ele.style.background = "white";
        document.getElementById("fmsg")
            .innerHTML = "Wait, we will refresh your window within next one minute";
        setTimeout(function() {
            location.reload()
        }, tymleft - 180000);
    } else if (tymleft < 180000 || tymleft > 604620000) {
        ele.style.background = "springgreen";
        if (refresh) {
            document.getElementById("fmsg")
                .innerHTML = "Tried to click " + ti + " times";
            if (ti == refresh * 10){ location.reload(); return;}
            else if((tymleft < 90000 || tymleft > 604710000) && !(ti%10)) trycallapi(eid);
        } else document.getElementById("fmsg")
            .innerHTML = "we have tried to click it for you " + ti + " times";
        if ($s( "span:contains('BUY NOW')" ).length) {
            // if (fkoco) setCookie("fsocb", fkoco, 30, "/checkout/init");
            // setCookie("CONG", 1, 180, "/");
            history.pushState(null, null, location.href);
            callapi(eid, mobdate, mobname, refresh);
        }
        else if (ti < 4200) {
            ti++;
            setTimeout(function() {
                FK3buy(mobdate, mobname, refresh);
            }, 100);

        }
    } else if (tymleft > 601200000 && tymleft < 604795000) {
        ele.remove();
    }


}
function callapi(id, mobdate, mobname, refresh) {
    apic++;
    ti++;
    var httpq4 = new getXMLHTTPRequest();
    httpq4.open("POST", '/api/5/cart', true);
    httpq4.onreadystatechange = function() {
        if (httpq4.readyState == 4) {
            if (httpq4.status == 200) {
                var mytext = httpq4.responseText;

                try {
                    if (JSON.parse(mytext)['RESPONSE']['cartResponse'][id]['presentInCart'] == true) {
                        virwcart();
                    } else if (apic < 14) return setTimeout(function() {
                        FK3buy(mobdate, mobname, refresh);
                    }, apic * 100);
                    else document.getElementById("fmsg")
                        .innerHTML = "Some error occured, please try manually";
                } catch (err) {
                    if (apic < 14) return setTimeout(function() {
                        FK3buy(mobdate, mobname, refresh);
                    }, apic * 100);
                    else document.getElementById("fmsg")
                        .innerHTML = "Some error occured, please try manually";
                }
            }
        }
    };
    httpq4.setRequestHeader("Content-type", "application/json");
    httpq4.setRequestHeader('X-user-agent', navigator.userAgent + ' FKUA/website/41/website/Desktop');
    httpq4.send('{"cartContext":{"' + id + '":{"quantity":1}}}');
}
function getXMLHTTPRequest() {
    req = new XMLHttpRequest();
    return req;
}


function trycallapi(id) {
    var httpq4 = new getXMLHTTPRequest();
    httpq4.open("POST", '/api/5/cart', true);
    httpq4.onreadystatechange = function() {
        if (httpq4.readyState == 4) {
            if (httpq4.status == 200) {
                var mytext = httpq4.responseText;
                try {
                    // virwcart();
                    if (JSON.parse(mytext)['RESPONSE']['cartResponse'][id]['presentInCart'] == true) {
                        if (fkoco) setCookie("fsocb", fkoco, 30, "/checkout/init");
                        setCookie("CONG", 1, 180, "/");
                        history.pushState(null, null, location.href);
                        virwcart();
                        return true;
                    }
                } catch (err) {
                    return false;
                }
            }
        }
    };
    httpq4.setRequestHeader("Content-type", "application/json");
    httpq4.setRequestHeader('X-user-agent', navigator.userAgent + ' FKUA/website/41/website/Desktop');
    httpq4.send('{"cartContext":{"' + id + '":{"quantity":1}}}');
}

function virwcart(){
    window.location = 'https://www.flipkart.com/rv/viewcart';
}