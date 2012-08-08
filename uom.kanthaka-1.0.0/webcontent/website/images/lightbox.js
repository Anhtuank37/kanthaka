function lightbox() {
	var thisClass = this;
	this.overlay = $(jQuery('<div id="overlay" style="position: absolute; display: none; z-index: 1002;"></div>'));
	this.container = $(jQuery('<div id="lightbox" style="position: absolute; display: none; z-index: 1003; padding: 10;"></div>'));
	this.close = $(jQuery('<a href="#close" class="close">&times; Close</a>'));
	this.target = $(jQuery('<div class="target"></div>'));
	//this.prev = $(jQuery('<a href="#prev" class="prev">&laquo; Previous</a>'));
	//this.next = $(jQuery('<a href="#next" class="next">Next &raquo;</a>'));

	this.overlay.css({opacity: 0.25});
	$('body').append(this.overlay).append(this.container);
	this.container.append(this.close).append(this.target); //.append(this.prev).append(this.next);
	var op = this.container.css("opacity");
	this.container.css("opacity", 0);
	this.container.show().css({'top': Math.round((($(window).height() > window.innerHeight ? window.innerHeight : $(window).height()) - this.container.outerHeight()) / 2) + 'px', 'left': Math.round(($(window).width() - this.container.outerWidth()) / 2) + 'px', 'margin-top': 0, 'margin-left': 0}).hide();
	this.container.css("opacity", op);

	this.close.click(function(c) {
		c.preventDefault();
		thisClass.overlay.add(thisClass.container).fadeOut('normal');
	});

	this.target.click(function(c) {
		c.preventDefault();
		thisClass.overlay.add(thisClass.container).fadeOut('normal');
	});

	this.overlay.click(function(c) {
		c.preventDefault();
		thisClass.overlay.add(thisClass.container).fadeOut('normal');
	});
}

lightbox.prototype.links = null;
lightbox.prototype.overlay = null;
lightbox.prototype.container = null;
lightbox.prototype.close = null;
lightbox.prototype.target = null;
lightbox.prototype.prev = null;
lightbox.prototype.next = null;

lightbox.prototype.parse = function() {
  var thisClass = this;
  links = $('a[rel^=lightbox]');
  links.each(function(index) {
    var link = $(this);
    link.click(function(c) {
      c.preventDefault();
      thisClass.open(link.attr('href'));
      links.filter('.selected').removeClass('selected');
      link.addClass('selected');
    });
    link.attr({'lb-position': index});
  });

/*
  this.prev.add(this.next).click(function(c) {
    c.preventDefault();
    var current = parseInt(links.filter('.selected').attr('lb-position'),10);
    var to = $(this).is('.prev') ? links.eq(current - 1) : links.eq(current + 1);
    if(!to.size()) {
      to = $(this).is('.prev') ? links.eq(links.size() - 1) : links.eq(0);
    }
    if(to.size()) {
      to.click();
    }
  });
*/
}

lightbox.prototype.open = function(url) {
	var thisClass = this;
	// make sure they are in the very end of all elements
  $('body').append(this.overlay).append(this.container);
    if(this.container.is(':visible')) {
      this.target.children().fadeOut('normal', function() {
        thisClass.target.children().remove();
        thisClass.loadImage(url);
      });
    } else {
      this.target.children().remove();
      this.overlay.add(this.container).fadeIn('normal',function(){
        thisClass.loadImage(url);
      });
    }
  }
;
lightbox.prototype.loadImage = function(url) {
  var thisClass = this;
  if(this.container.is('.loading')) { return; }
  this.container.addClass('loading');
  var img = new Image();
  img.onload = function() {
    img.style.display = 'none';

    var maxWidth = ($(window).width() - parseInt(thisClass.container.css('padding-left'),10) - parseInt(thisClass.container.css('padding-right'), 10)) - 100;
    var maxHeight = (($(window).height() > window.innerHeight ? window.innerHeight : $(window).height()) - parseInt(thisClass.container.css('padding-top'),10) - parseInt(thisClass.container.css('padding-bottom'), 10)) - 100;

	if(img.width > maxWidth || img.height > maxHeight) { // One of these is larger than the window
		var ratio1 = img.width / maxWidth;
		var ratio2 = img.height / maxHeight;
		var ratio = (ratio1>ratio2)?ratio1:ratio2;
		img.width = Math.floor(img.width / ratio);
		img.height = Math.floor(img.height / ratio);
	}
/*
    if(img.width > maxWidth || img.height > maxHeight) { // One of these is larger than the window
      var ratio = img.width / img.height;
      if(img.height >= maxHeight) {
        img.height = maxHeight;
        img.width = maxHeight * ratio;
      } else {
        img.width = maxWidth;
        img.height = maxWidth * ratio;
      }
    }
*/
    thisClass.container.animate({'width': img.width,'height': img.height, 'top': Math.round((($(window).height() > window.innerHeight ? window.innerHeight : $(window).height()) - img.height - parseInt(thisClass.container.css('padding-top'),10) - parseInt(thisClass.container.css('padding-bottom'),10)) / 2) + 'px', 'left': Math.round(($(window).width() - img.width - parseInt(thisClass.container.css('padding-left'),10) - parseInt(thisClass.container.css('padding-right'),10)) / 2) + 'px'},'normal', function(){
      thisClass.target.append(img);
      $(img).fadeIn('normal', function() {
        thisClass.container.removeClass('loading');
      });
    })
  }
  img.src = url;
};

var LightBox = null;
$().ready(function() {
	LightBox = new lightbox();
});