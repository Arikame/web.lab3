window.onload = function () {
    <!--чтобы все нарисовалось и вывелись старые старые точки сразу при загрузке-->
    redraw();
}

let canvas = document.getElementById("canvas"),
    context = canvas.getContext("2d");

let hiddenTable = document.getElementById("bottom");
hiddenTable.style.display = "none";

let unhidden = function(){
    hiddenTable.style.display = "block";
};

let getR = function () {
    let R = document.getElementById("PointForm:variableR").value;
    return R;
};

let cleanCanvas = function() {
    context.clearRect(0, 0, canvas.width, canvas.height);
    let R = getR();
    let R2 = R/2;

//прямоугольник
    context.beginPath();
    context.rect(200, 150, 50, 100);
    context.closePath();
    context.strokeStyle = "#4899FF";
    context.fillStyle = "#4899FF";
    context.fill();
    context.stroke();

    // сектор
    context.beginPath();
    context.moveTo(200, 150);
    context.arc(200, 150, 100, Math.PI / 2, Math.PI);
    context.moveTo(200, 150);
    context.closePath();
    context.strokeStyle = "#4899FF";
    context.fillStyle = "#4899FF";
    context.fill();
    context.stroke();

    //уголок
    context.beginPath();
    context.moveTo(200, 150);
    context.lineTo(200, 50);
    context.lineTo(300, 150);
    context.lineTo(200, 150);
    context.closePath();
    context.strokeStyle = "#4899FF";
    context.fillStyle = "#4899FF";
    context.fill();
    context.stroke();

//отрисовка осей
    context.beginPath();
    context.strokeStyle = "black";
    context.font = "14px Comic Sans MS";
    context.moveTo(200, 0);
    context.lineTo(200, 300);
    context.fillStyle = "black";
    context.stroke();
    context.moveTo(195, 200);
    context.lineTo(205, 200);
    context.stroke();
    context.fillText("-"+R2, 206, 196);
    context.moveTo(195, 250);
    context.lineTo(205, 250);
    context.stroke();
    context.fillText("-"+R, 206, 246);

    context.moveTo(195, 50);
    context.lineTo(205, 50);
    context.stroke();
    context.fillText(R, 206, 46);

    context.moveTo(195, 100);
    context.lineTo(205, 100);
    context.stroke();
    context.fillText(R2, 206, 96); //X = 0

    context.moveTo(100, 147);
    context.lineTo(100, 153);
    context.stroke();
    context.fillText("-"+R, 102, 147);

    context.moveTo(150, 147);
    context.lineTo(150, 153);
    context.stroke();
    context.fillText("-"+R2, 152, 147);

    context.moveTo(300, 147);
    context.lineTo(300, 153);
    context.stroke();
    context.fillText(R, 302, 147);

    context.fillText("Y", 205, 10);
    context.moveTo(0, 150);
    context.lineTo(400, 150);
    context.stroke();

    context.fillText("X", 390, 140);


    context.closePath();
    context.stroke();
}

let redraw = function() {
    let R = getR();
    cleanCanvas();
    needList([{name:'Radius', value: R}]);
}

cleanCanvas();
let mouseX;
let mouseY;
let mouseXshow;
let mouseYshow;

canvas.addEventListener('click', function(e) {
    let R = getR();
    let shout = sendClick([{name:'PointX', value: mouseXshow},{name:'PointY', value: mouseYshow},{name:'PointR', value: getR()}]);
}, false);

//составляем строку из текущих выбранных boleancheckbox
let manyXtoOne = function(){
    let str = "";
    if (document.getElementById("PointForm:X_3").checked)
        str += -3;
    if (document.getElementById("PointForm:X_2").checked) {
        if (str != "")
            str += " ";
        str += -2;
    }
    if (document.getElementById("PointForm:X_1").checked) {
        if (str != "")
            str += " ";
        str += -1;
    }
    if (document.getElementById("PointForm:X0").checked) {
        if (str != "")
            str += " ";
        str += 0;
    }
    if (document.getElementById("PointForm:X1").checked) {
        if (str != "")
            str += " ";
        str += 1;
    }
    if (document.getElementById("PointForm:X2").checked) {
        if (str != "")
            str += " ";
        str += 2;
    }
    if (document.getElementById("PointForm:X3").checked) {
        if (str != "")
            str += " ";
        str += 3;
    }
    if (document.getElementById("PointForm:X4").checked) {
        if (str != "")
            str += " ";
        str += 4;
    }
    if (document.getElementById("PointForm:X5").checked) {
        if (str != "")
            str += " ";
        str += 5;
    }
    document.getElementById("PointForm:variableX").value = str;
}

let getPointColor =  function (itsShot){
    let color;
    if (itsShot == 1) {
        color = "green";
    } else {
        color = "red";
    }
    return color;
};
let drawPoint = function (x,y,r,shout){
    let xRes = parseFloat(x) * 100 / parseFloat(r) + 200;
    let yRes = parseFloat(y) * -1 * 100 / parseFloat(r) + 150;
    console.log("x: " + x + " y: " + y + " r: " + r + " shout: " + shout + " xRes: " + xRes + " yRes: " + yRes);
    shout = parseInt(shout);
    if (shout != 4) {
        context.beginPath();
        context.arc(xRes, yRes, 2, 0, 2 * Math.PI, false);
        context.closePath();
        let color = getPointColor(shout);
        context.strokeStyle = color;
        context.fillStyle = color;
        context.fill();
        context.stroke();
    }
};

let drawAlotOfPoints = function(ListPoints) {
    let jsonResponsePoints = JSON.parse(ListPoints);
    jsonResponsePoints.forEach(function (value, index, array) {
        let xRes = parseFloat(value.x) * 100 / R + 200;
        let yRes = parseFloat(value.y) * -1 * 100 / R + 150;
        context.beginPath();
        let shout1 = value.result;
        context.arc(xRes, yRes, 2, 0, 2 * Math.PI, false);
        context.closePath();
        let color = getPointColor(shout1);
        context.strokeStyle = color;
        context.fillStyle = color;
        context.fill();
        context.stroke();
        console.log("xres= "+xRes + "yres = " +yRes + " result = " +value.result);
    });
};

function getCursorPosition(e) {
    let R = getR();
    mouseX = e.offsetX;
    mouseY = e.offsetY;
    //console.log(mouseX);
    mouseXshow = (mouseX - 200) / 100 * R;
    mouseYshow = (mouseY - 150) / 100 * R * -1;
    if (R != "R") {
        document.getElementById("movelog").innerHTML = "X: " + mouseXshow + " / Y: " + mouseYshow + "/ R:" + R;
    }
    else{
        document.getElementById("movelog").innerHTML = "X: / Y: / R: ";
    }
}
canvas.addEventListener('mousemove', getCursorPosition, false);

