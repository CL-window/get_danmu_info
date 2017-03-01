/**475
enc = yes
command = receivemessage
content = VVHLboMwEPyXPfSEGmzjF1JUVe2lUk) NseRAwRCrYCwwaaIo) 941 JFJ72Zd3dmbXF2i6soUc0pRAAtXggnEB8guEszdvr5DTlCQQeshJpqQmXGmaQGNrxAiumNZaIbAZlwolmhJKFVlKA6KgmGvFarSVMsUsZE3vNjOEFTOvuPnbg8gwLDjKZYpW8mi5qGL8JYu5MVlTzFLIOI8IrLBVfYgauBZUa6YxXTVxoWSUOHlM0P(YsguHj9J9407ynr(XwUDOUkF4yuMhrBNrj7hltw6K6) txwGGfILnQOFFqRmCPhLf6) t8hbTDxegm4Gb1GYf3UrlKmQ(lN3HVTet(ZYtPaJuxisdicrHs89d3TcUse4l9s44bx) QXFQJ4mYKfdio8xkjyP55W8dWVvVoZ2HGa) EJMrfokf7REfJCxil1jD9foL "
**/
var a, b, c = null,
    f, e, g, k, m, l, p, n, q, u, t, v, r, w, z, y = [0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535],
    A = [3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 15, 17, 19, 23, 27, 31, 35, 43, 51, 59, 67, 83, 99, 115, 131, 163, 195, 227, 258, 0, 0],
    C = [0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 0, 99, 99],
    x = [1, 2, 3, 4, 5, 7, 9, 13, 17, 25, 33, 49, 65, 97, 129, 193, 257, 385, 513,
        769, 1025, 1537, 2049, 3073, 4097, 6145, 8193, 12289, 16385, 24577
    ],
    F = [0, 0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 13, 13],
    K = [16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15],
    B = function() { this.list = this.next = null },
    Y = function() {
        this.n = this.b = this.e = 0;
        this.t = null
    },
    U = function(a, b, c, f, e, d) {
        this.BMAX = 16;
        this.N_MAX = 288;
        this.status = 0;
        this.root = null;
        this.m = 0;
        var g = Array(this.BMAX + 1),
            k, l, m, q, r, n, h, v = Array(this.BMAX + 1),
            p, t, u, y = new Y,
            C = Array(this.BMAX);
        q = Array(this.N_MAX);
        var w, A = Array(this.BMAX + 1),
            x,
            z, F;
        F = this.root = null;
        for (r = 0; r < g.length; r++) g[r] = 0;
        for (r = 0; r < v.length; r++) v[r] = 0;
        for (r = 0; r < C.length; r++) C[r] = null;
        for (r = 0; r < q.length; r++) q[r] = 0;
        for (r = 0; r < A.length; r++) A[r] = 0;
        k = 256 < b ? a[256] : this.BMAX;
        p = a;
        t = 0;
        r = b;
        do g[p[t]]++, t++; while (0 < --r);
        if (g[0] == b) this.root = null, this.status = this.m = 0;
        else {
            for (n = 1; n <= this.BMAX && 0 == g[n]; n++);
            h = n;
            d < n && (d = n);
            for (r = this.BMAX; 0 != r && 0 == g[r]; r--);
            m = r;
            d > r && (d = r);
            for (x = 1 << n; n < r; n++, x <<= 1)
                if (0 > (x -= g[n])) {
                    this.status = 2;
                    this.m = d;
                    return
                }
            if (0 > (x -= g[r])) this.status = 2, this.m = d;
            else {
                g[r] += x;
                A[1] = n = 0;
                p = g;
                t = 1;
                for (u = 2; 0 < --r;) A[u++] = n += p[t++];
                p = a;
                r = t = 0;
                do 0 != (n = p[t++]) && (q[A[n]++] = r); while (++r < b);
                b = A[m];
                A[0] = r = 0;
                p = q;
                t = 0;
                q = -1;
                w = v[0] = 0;
                u = null;
                for (z = 0; h <= m; h++)
                    for (a = g[h]; 0 < a--;) {
                        for (; h > w + v[1 + q];) {
                            w += v[1 + q];
                            q++;
                            z = (z = m - w) > d ? d : z;
                            if ((l = 1 << (n = h - w)) > a + 1)
                                for (l -= a + 1, u = h; ++n < z && !((l <<= 1) <= g[++u]);) l -= g[u];
                            w + n > k && w < k && (n = k - w);
                            z = 1 << n;
                            v[1 + q] = n;
                            u = Array(z);
                            for (l = 0; l < z; l++) u[l] = new Y;
                            F = null == F ? this.root = new B : F.next = new B;
                            F.next = null;
                            F.list = u;
                            C[q] = u;
                            0 < q && (A[q] = r, y.b = v[q], y.e = 16 + n, y.t = u, n = (r &
                                (1 << w) - 1) >> w - v[q], C[q - 1][n].e = y.e, C[q - 1][n].b = y.b, C[q - 1][n].n = y.n, C[q - 1][n].t = y.t)
                        }
                        y.b = h - w;
                        t >= b ? y.e = 99 : p[t] < c ? (y.e = 256 > p[t] ? 16 : 15, y.n = p[t++]) : (y.e = e[p[t] - c], y.n = f[p[t++] - c]);
                        l = 1 << h - w;
                        for (n = r >> w; n < z; n += l) u[n].e = y.e, u[n].b = y.b, u[n].n = y.n, u[n].t = y.t;
                        for (n = 1 << h - 1; 0 != (r & n); n >>= 1) r ^= n;
                        for (r ^= n;
                            (r & (1 << w) - 1) != A[q];) w -= v[q], q--
                    }
                this.m = v[1];
                this.status = 0 != x && 1 != m ? 1 : 0
            }
        }
    },
    H = function(a) {
        for (; m < a;) {
            var b = k,
                c;
            c = w.length == z ? -1 : w.charCodeAt(z++) & 255;
            k = b | c << m;
            m += 8
        }
    },
    I = function(a) { return k & y[a] },
    E = function(a) {
        k >>= a;
        m -= a
    },
    N = function(c, f, e) {
        var g, d, k;
        if (0 == e) return 0;
        for (k = 0;;) {
            H(v);
            d = u.list[I(v)];
            for (g = d.e; 16 < g;) {
                if (99 == g) return -1;
                E(d.b);
                g -= 16;
                H(g);
                d = d.t[I(g)];
                g = d.e
            }
            E(d.b);
            if (16 == g) b &= 32767, c[f + k++] = a[b++] = d.n;
            else {
                if (15 == g) break;
                H(g);
                n = d.n + I(g);
                E(g);
                H(r);
                d = t.list[I(r)];
                for (g = d.e; 16 < g;) {
                    if (99 == g) return -1;
                    E(d.b);
                    g -= 16;
                    H(g);
                    d = d.t[I(g)];
                    g = d.e
                }
                E(d.b);
                H(g);
                q = b - d.n - I(g);
                for (E(g); 0 < n && k < e;) n--, q &= 32767, b &= 32767, c[f + k++] = a[b++] = a[q++]
            }
            if (k == e) return e
        }
        l = -1;
        return k
    },
    R = function(a, b, c) {
        var f, e, g, d, k, l, m, q = Array(316);
        for (f =
            0; f < q.length; f++) q[f] = 0;
        H(5);
        l = 257 + I(5);
        E(5);
        H(5);
        m = 1 + I(5);
        E(5);
        H(4);
        f = 4 + I(4);
        E(4);
        if (286 < l || 30 < m) return -1;
        for (e = 0; e < f; e++) H(3), q[K[e]] = I(3), E(3);
        for (; 19 > e; e++) q[K[e]] = 0;
        v = 7;
        e = new U(q, 19, 19, null, null, v);
        if (0 != e.status) return -1;
        u = e.root;
        v = e.m;
        d = l + m;
        for (f = g = 0; f < d;)
            if (H(v), k = u.list[I(v)], e = k.b, E(e), e = k.n, 16 > e) q[f++] = g = e;
            else if (16 == e) {
            H(2);
            e = 3 + I(2);
            E(2);
            if (f + e > d) return -1;
            for (; 0 < e--;) q[f++] = g
        } else {
            17 == e ? (H(3), e = 3 + I(3), E(3)) : (H(7), e = 11 + I(7), E(7));
            if (f + e > d) return -1;
            for (; 0 < e--;) q[f++] = 0;
            g = 0
        }
        v = 9;
        e = new U(q,
            l, 257, A, C, v);
        0 == v && (e.status = 1);
        if (0 != e.status) return -1;
        u = e.root;
        v = e.m;
        for (f = 0; f < m; f++) q[f] = q[f + l];
        r = 6;
        e = new U(q, m, 0, x, F, r);
        t = e.root;
        r = e.m;
        return 0 == r && 257 < l || 0 != e.status ? -1 : N(a, b, c)
    },
    V = function(d, h, y) {
        var w, z;
        for (w = 0; w < y && (!p || -1 != l);) {
            if (0 < n) {
                if (0 != l)
                    for (; 0 < n && w < y;) n--, q &= 32767, b &= 32767, d[h + w++] = a[b++] = a[q++];
                else {
                    for (; 0 < n && w < y;) n--, b &= 32767, H(8), d[h + w++] = a[b++] = I(8), E(8);
                    0 == n && (l = -1)
                }
                if (w == y) break
            }
            if (-1 == l) {
                if (p) break;
                H(1);
                0 != I(1) && (p = !0);
                E(1);
                H(2);
                l = I(2);
                E(2);
                u = null;
                n = 0
            }
            switch (l) {
                case 0:
                    z = d;
                    var K =
                        h + w,
                        B = y - w,
                        G = void 0,
                        G = m & 7;
                    E(G);
                    H(16);
                    G = I(16);
                    E(16);
                    H(16);
                    if (G != (~k & 65535)) z = -1;
                    else {
                        E(16);
                        n = G;
                        for (G = 0; 0 < n && G < B;) n--, b &= 32767, H(8), z[K + G++] = a[b++] = I(8), E(8);
                        0 == n && (l = -1);
                        z = G
                    }
                    break;
                case 1:
                    if (null != u) z = N(d, h + w, y - w);
                    else a: {
                        z = d;K = h + w;B = y - w;
                        if (null == c) {
                            for (var D = void 0, G = Array(288), D = void 0, D = 0; 144 > D; D++) G[D] = 8;
                            for (; 256 > D; D++) G[D] = 9;
                            for (; 280 > D; D++) G[D] = 7;
                            for (; 288 > D; D++) G[D] = 8;
                            e = 7;
                            D = new U(G, 288, 257, A, C, e);
                            if (0 != D.status) {
                                alert("HufBuild error: " + D.status);
                                z = -1;
                                break a
                            }
                            c = D.root;
                            e = D.m;
                            for (D = 0; 30 > D; D++) G[D] =
                                5;
                            g = 5;
                            D = new U(G, 30, 0, x, F, g);
                            if (1 < D.status) {
                                c = null;
                                alert("HufBuild error: " + D.status);
                                z = -1;
                                break a
                            }
                            f = D.root;
                            g = D.m
                        }
                        u = c;t = f;v = e;r = g;z = N(z, K, B)
                    }
                    break;
                case 2:
                    z = null != u ? N(d, h + w, y - w) : R(d, h + w, y - w);
                    break;
                default:
                    z = -1
            }
            if (-1 == z) return p ? 0 : -1;
            w += z
        }
        return w
    };


var decode2 = function(c) {
    var e;
    null == a && (a = Array(65536));
    m = k = b = 0;
    l = -1;
    p = !1;
    n = q = 0;
    u = null;
    w = c;
    z = 0;
    for (var f = Array(1024), g = []; 0 < (c = V(f, 0, f.length));) {
        var d = Array(c);
        for (e = 0; e < c; e++) d[e] = String.fromCharCode(f[e]);
        g[g.length] = d.join("")
    }
    w = null;
    return g.join("")
};

var decode = function(a) {
    a = a.replace(/\(|\)|\@/g, function (a) {
        switch (a) {
            case "(":
                return "+";
            case ")":
                return "/";
            case "@":
                return "="
        }
    });
    a = window.atob(a);
    return decode2(a);
};

var startHeartBeat = function() {
    window.setInterval(function() {
        /**console.log("send heart beat");*/
        socket.send("command=sendmessage\r\ncontent=y8vPLwAA\r\n");
    }, 5000);
};

var getUid = function(argument) {
    return Date.parse(new Date())/1000;

};

var socket;

function sixRoomListener(roomId,address){
    LiveJsChat.onLogin("sixRoomListener " + roomId + " " + address);
    var ROOM_ID = roomId;
    var WEBSOCKET_ADDR = address;

    socket = new WebSocket('ws://'+WEBSOCKET_ADDR);

    socket.onopen = function(event) {
        socket.send("command=login\r\nuid=" + getUid() + "\r\nencpass=\r\nroomid=" + ROOM_ID + "\r\n");
    };

    socket.onclose = function(event) {
        console.log('Client notified socket has closed',event);
        LiveJsChat.onLogout();
    };

    socket.onmessage = function(event) {

        var buf = event.data;
        var list = buf.split("\r\n");
        var enc = false;
        var data = {};
        for(i=1; i<list.length-1; i++){
            var tmp = list[i].split("=");
            data[tmp[0]] = tmp[1];
        };
        if(data.enc=="yes"){
            data.content = decode(data.content);
        }else{
            try{
                data.content = window.atob(data.content);
            }catch(e){ }
        };

        console.log("all: \n" +  data.command +'\n'+ data.content);
        if(data.command=="result"){

            if(data.content=="login.success"){
                console.log("login suc");
                LiveJsChat.onLogin();
                /**这里是登录弹幕服务器成功*/
                startHeartBeat();
            }else{
                /** send.success 这里可能会收到服务端的心跳*/
                /**console.log(data.content);*/
            }
        }
        else if(data.command=="receivemessage"){
            /**这里是收到消息*/
            try{
                var content = (JSON.parse(data.content)).content;
                /** 聊天 chart */
                if(content.typeID == 101){
                    if(content.to){
                        LiveJsChat.onGetChart(content.from, content.to, content.content);
                    }else{
                        LiveJsChat.onGetChart(content.from, content.content);
                    }
                }
                else if(content.typeID == 201){
                    /**礼物 信息 */
                    var gift = content.content;
                    LiveJsChat.onGetGifts(content.from, content.to, gift.item, gift.num);
                }
                else if(content.typeID == 1413){
                    var c1413 = content.content;
                    var len = c1413.length;
                     /** 是个数组，可能同时有多个用户进入 */
                    for(var i=0;i<len;i++){
                        var cc = c1413[i];
                        if(cc.typeID == 123){
                            var userIn = cc.content;
                            if(userIn.alias){
                                LiveJsChat.onUserIn(userIn.alias, userIn.msg);
                            }else{
                                LiveJsChat.onUserIn(userIn.msg);
                            }
                        }else if(cc.typeID == 1304){
                            /**红包 财*/
                            LiveJsChat.onGetGiftsInSide(
                                cc.from,
                                "红包",
                                "http://vr0.6rooms.com/imges/live/CSSIMG/gift/redBag.png",
                                cc.content.num);
                        }else if(cc.typeID == 1309){
                            /**人气票*/
                            LiveJsChat.onGetGiftsInSide(
                                cc.from,
                                "人气票",
                                "http://vr0.6rooms.com/imges/live/CSSIMG/gift/event/gift_renqi_small_v3.png",
                                cc.content.num);

                        }
                    }

                }

            }catch(e){ }

        }else{
            /**这里会有什么我也不知道*/
            console.log("other " + data);
        };

    };

};




