function WB_Gallery(data) {
	this.content = document.getElementById(data.id);
	this.slideshowTimer = null;
	this.id = data.id;
	this.type = data.type;
	this.thumbWidth = data.thumbWidth;
	this.thumbHeight = data.thumbHeight;
	this.slideshowInterval = data.interval;
	this.images = data.images;
	this.galleryPath = data.path;
	this.imageIndex = 0;
	this.width = $(this.content).width();
	this.height = $(this.content).height();
	switch( this.type )
	{
		case "slideshow": this.renderSlideshow(); break;
		case "list": this.renderList(); break;
		default: this.renderThumbs(); break;
	}
}

WB_Gallery.prototype.type = "thumbs";
WB_Gallery.prototype.slideshowInterval = 10;
WB_Gallery.prototype.images = [];
WB_Gallery.prototype.imageIndex = 0;
WB_Gallery.prototype.slideshowTimer = null;
WB_Gallery.prototype.displayedImage = null;
WB_Gallery.prototype.loadingImage = null;
WB_Gallery.prototype.listImgCont = null;
WB_Gallery.prototype.thumbImgCont = null;
WB_Gallery.prototype.fullThumbWidth = 0;
WB_Gallery.prototype.fullThumbHeight = 0;
WB_Gallery.prototype.thumbWidth = 64;
WB_Gallery.prototype.thumbHeight = 64;
WB_Gallery.prototype.width = 320;
WB_Gallery.prototype.height = 240;
WB_Gallery.prototype.galleryPath = "";

WB_Gallery.prototype.renderThumbs = function() {
	this.displayedImage = null;
	this.loadingImage = null;
	this.content.innerHTML = '<div style="width: 100%; height: 100%; overflow: auto;"></div>';
	var elem = this.content.firstChild;
	for( var i = 0; i < this.images.length; i++ )
		this.addImage(elem, this.images[i], false);
}

WB_Gallery.prototype.renderSlideshow = function() {
	var thisClass = this;
	this.displayedImage = null;
	this.loadingImage = null;
	this.content.innerHTML =
		'<div class="gallery-slideshow">'+
		'<div class="gallery-slide-image" style="overflow: hidden;"></div>'+
		'<div class="gallery-slide-left"></div>'+
		'<div class="gallery-slide-right"></div>'+
		'</div>';
	this.listImgCont = this.content.firstChild.children[0];
	var cont = $(this.content.firstChild);
	var larr = $(this.content.firstChild.children[1]);
	var rarr = $(this.content.firstChild.children[2]);
	larr.css({left: "0", top: Math.floor((cont.innerHeight() - larr.height()) / 2) + "px"});
	rarr.css({right: "0", top: Math.floor((cont.innerHeight() - larr.height()) / 2) + "px"});

	larr.click(function() {
		thisClass.slideshowPrev();
	});

	rarr.click(function() {
		thisClass.slideshowNext();
	});

	this.slideshowTimer = setTimeout(function(){
		thisClass.slideshowNext();
	}, this.slideshowInterval * 1000);
	this.imageIndex = -1;
	this.slideshowNext();
}

WB_Gallery.prototype.renderList = function() {
	var thisClass = this;
	this.content.innerHTML = '';
	this.displayedImage = null;
	this.loadingImage = null;

	var w = this.width;
	var h = this.height;
	var tw = this.thumbWidth;
	var th = this.thumbHeight;

	var pad = 3;
	
	var thumbcont = document.createElement("DIV");

	//var images = [];
	for( var i = 0; i < this.images.length; i++ )
	{
	   var image = this.images[i];
	   var img_title = image.title;
	   var img_file = this.galleryPath + "/" + image.file;
	   var img_thumb = this.galleryPath + ".thumbs/" + image.thumb;
		var img = this.addImage(thumbcont, image, true);
		img.idx = i;
		$(img).css({cursor: "pointer"});
		$(img).click(function() {
		   thisClass.imageIndex = this.idx;
			thisClass.displayImage(thisClass.listImgCont);
			$(this.parentNode.parentNode.parentNode).children(".tmb-selected").removeClass("tmb-selected");
			$(this.parentNode.parentNode).addClass("tmb-selected");
		});
		var par_in = $(img.parentNode);
		var par = $(img.parentNode.parentNode);
		if( i == this.imageIndex )
		{
		   par.addClass("tmb-selected");
			tw = this.thumbWidth + 8;
			th = this.thumbHeight + 8;

//			tw = this.thumbWidth + parseInt(par.css("padding-left"), 0) + parseInt(par.css("padding-right"), 0) + parseInt(par.css("border-left-width"), 0) + parseInt(par.css("border-right-width"), 0) + parseInt(par.css("margin-left"), 0) + parseInt(par.css("margin-right"), 0);
//			th = this.thumbHeight + parseInt(par.css("padding-top"), 0) + parseInt(par.css("padding-bottom"), 0) + parseInt(par.css("border-top-width"), 0) + parseInt(par.css("border-bottom-width"), 0) + parseInt(par.css("margin-top"), 0) + parseInt(par.css("margin-bottom"), 0);
//			tw += parseInt(par_in.css("padding-left"), 0) + parseInt(par_in.css("padding-right"), 0) + parseInt(par_in.css("border-left-width"), 0) + parseInt(par_in.css("border-right-width"), 0) + parseInt(par_in.css("margin-left"), 0) + parseInt(par_in.css("margin-right"), 0);
//			th += parseInt(par_in.css("padding-top"), 0) + parseInt(par_in.css("padding-bottom"), 0) + parseInt(par_in.css("border-top-width"), 0) + parseInt(par_in.css("border-bottom-width"), 0) + parseInt(par_in.css("margin-top"), 0) + parseInt(par_in.css("margin-bottom"), 0);
  		}
	}

	this.fullThumbWidth = tw;
	this.fullThumbHeight = th;

	$(thumbcont).css({position: "relative", position: "absolute", left: "0", top: "5px", width: (tw * this.images.length) + "px", height: th + "px"});

	var galcont = document.createElement("DIV");
	$(galcont).css({position: "relative", width: w + "px", height: h + "px"});
	galcont.className = "gallery-list";

	var imgcont = document.createElement("DIV");
	$(imgcont).css({position: "relative", width: w + "px", height: (h - th - 10) + "px", overflow: "hidden"});
	imgcont.className = "gallery-list-image";

	var thumbdiv_in1 = document.createElement("DIV");
	$(thumbdiv_in1).css({position: "relative", "float": "left", width: "16px", height: (th + 10) + "px", cursor: "pointer"});
	thumbdiv_in1.className = "gallery-list-left";
	$(thumbdiv_in1).click(function() {
		thisClass.slideBy(-thisClass.fullThumbWidth * 3);
	});

	var thumbdiv_in2 = document.createElement("DIV");
	$(thumbdiv_in2).css({position: "relative", "float": "left", width: (w-32) + "px", height: (th + 10) + "px", overflow: "hidden"});
	thumbdiv_in2.className = "gallery-list-thumbs";

	var thumbdiv_in3 = document.createElement("DIV");
	$(thumbdiv_in3).css({position: "relative", "float": "left", width: "16px", height: (th + 10) + "px", cursor: "pointer"});
	thumbdiv_in3.className = "gallery-list-right";
	$(thumbdiv_in3).click(function() {
		thisClass.slideBy(thisClass.fullThumbWidth * 3);
	});

	var thumbdiv = document.createElement("DIV");
	$(thumbdiv).css({position: "relative", width: w + "px", height: (th + 10) + "px", overflow: "hidden"});

	thumbdiv_in2.appendChild(thumbcont);

	thumbdiv.appendChild(thumbdiv_in1);
	thumbdiv.appendChild(thumbdiv_in2);
	thumbdiv.appendChild(thumbdiv_in3);

	galcont.appendChild(imgcont);
	galcont.appendChild(thumbdiv);

	this.listImgCont = imgcont;
	this.thumbImgCont = thumbdiv_in2;
	this.content.appendChild(galcont);
	this.displayImage(this.listImgCont);
}

WB_Gallery.prototype.slideBy = function(delta) {
   var cont = $(this.thumbImgCont);
   var div = $(this.thumbImgCont.firstChild);
   var pos = div.position();
   var x = pos.left;
   x -= delta;
   var minx = -(div.width() - cont.width());
   if( x < minx ) x = minx;
   if( x > 0 ) x = 0;
	div.animate({left: x + "px"});
}

WB_Gallery.prototype.calcImageStyles = function(displayCont, cont, image)
{
	var jcont = $(cont);
	var dcont = $(displayCont);

	var minpadding = 5;
	var maxWidth = (dcont.innerWidth() - minpadding * 4);
	var maxHeight = (dcont.innerHeight() - minpadding * 4);

//	var padleft = Math.floor((jcont.outerWidth(true) - jcont.width()) / 2.0) + minpadding;
//	var padtop = Math.floor((jcont.outerHeight(true) - jcont.height()) / 2.0) + minpadding;
//	var padright = Math.ceil((jcont.outerWidth(true) - jcont.width()) / 2.0) + minpadding;
//	var padbot = Math.ceil((jcont.outerHeight(true) - jcont.height()) / 2.0) + minpadding;
//	var maxWidth = dcont.innerWidth() - padleft - padright;
//	var maxHeight = dcont.innerHeight() - padtop - padbot;

//	var padleft = parseInt(jcont.css('padding-left'),0) + parseInt(jcont.css('margin-left'),0) + parseInt(jcont.css('border-left-width'),0) + minpadding;
//	var padtop = parseInt(jcont.css('padding-top'),0) + parseInt(jcont.css('margin-top'),0) + parseInt(jcont.css('border-top-width'), 0) + minpadding;
//	var maxWidth = (dcont.innerWidth() - padleft - parseInt(jcont.css('padding-right'), 0) - parseInt(jcont.css('margin-right'),0) - parseInt(jcont.css('border-right-width'),0)) - minpadding;
//	var maxHeight = (dcont.innerHeight() - padtop - parseInt(jcont.css('padding-bottom'), 0) - parseInt(jcont.css('margin-bottom'),0) - parseInt(jcont.css('border-bottom-width'),0)) - minpadding;

	var w = image.width;
	var h = image.height;
	if(w > maxWidth || h > maxHeight) { // One of these is larger than the window
		var ratio1 = w / maxWidth;
		var ratio2 = h / maxHeight;
		var ratio = (ratio1>ratio2)?ratio1:ratio2;
		w = Math.floor(w / ratio);
		h = Math.floor(h / ratio);
	}
	return {
		left: Math.floor((maxWidth - w) / 2 + minpadding),
		top: Math.floor((maxHeight - h) / 2 + minpadding),
		image_width: w,
		image_height: h
	};
}

WB_Gallery.prototype.displayImage = function(display_cont) {
	var thisClass = this;

	var displayCont = display_cont;
	var image = this.images[this.imageIndex];
	if( !image )
	   return;
	var cont = document.createElement("DIV");
	cont.imageIndex = this.imageIndex;
	cont.title = image.title;
	var jcont = $(cont);
	cont.className = "gallery-image";

	var stl = this.calcImageStyles(displayCont, cont, image);

	jcont.css({
		display: "none",
		position: "absolute",
		left: stl.left + "px",
		top: stl.top + "px"
		});
	var img = new Image();
	cont.appendChild(img);
	img.width = stl.image_width;
	img.height = stl.image_height;
	img.alt = image.title;
	img.onload = function() {
		if( thisClass.displayedImage )
			$(thisClass.displayedImage).fadeOut('normal');
		jcont.fadeIn('normal', function() {
			if( thisClass.displayedImage )
			{
			   try {
					displayCont.removeChild(thisClass.displayedImage);
				}
				catch(ex) {
				}
			}
			if( cont.parentNode )
				thisClass.displayedImage = cont;
			else
				thisClass.displayedImage = null;
			thisClass.loadingImage = null;
			if( thisClass.slideshowTimer )
			{
			   clearTimeout(thisClass.slideshowTimer);
				thisClass.slideshowTimer = setTimeout(function(){
					thisClass.slideshowNext();
				}, thisClass.slideshowInterval * 1000);
			}
		});
	}
	displayCont.appendChild(cont);
   this.loadingImage = cont;
	img.src = this.galleryPath + "/" + image.file;
}

WB_Gallery.prototype.slideshowNext = function() {
	var thisClass = this;
	if( this.images.length == 0 )
		return;
	this.imageIndex++;
	if( this.imageIndex >= this.images.length )
		this.imageIndex = 0;
	this.displayImage(this.content.firstChild.firstChild);
}

WB_Gallery.prototype.slideshowPrev = function() {
	var thisClass = this;
	if( this.images.length == 0 )
		return;
	this.imageIndex--;
	if( this.imageIndex < 0 )
		this.imageIndex = this.images.length - 1;
	this.displayImage(this.content.firstChild.firstChild);
}

WB_Gallery.prototype.addImage = function(cont, image, nolightbox)
{
	var thisClass = this;
	var img_file = image.file;	// need to copy to prevent errors in callbacks
	var img_thumb_url = this.galleryPath + ".thumbs/" + image.thumb;
	var img_url = this.galleryPath + "/" + img_file;

	var div = document.createElement("DIV");
	div.className = "thumbnail";

	var bdiv = document.createElement("DIV");
	bdiv.title = image.title;

	var img = document.createElement("IMG");
	img.src = img_thumb_url;
	img.width = this.thumbWidth;
	img.height = this.thumbHeight;
	img.alt = image.title;
	$(img).css({zIndex: "1"});
	if( !nolightbox )
	{
		$(img).css({cursor: "pointer"});
		$(img).click(function() {
			LightBox.open(img_url);
		});
	}
	bdiv.appendChild(img);

	div.appendChild(bdiv);
	cont.appendChild(div);
	return img;
}

