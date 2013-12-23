
;(function($) {

  var showmore = 'showmore',
      defaults = {
        speed: 100,
        maxHeight: 200,
        showEach: 0,
        //moreLink: '<a href="#">Show More</a>',
        moreLink: '<div><input type="button" value="Show More" style="width:80%;height:35;font-size:15;"></div>',
        //moreLink: '<div><input type="button" value="Show More" class="ssql_showmore" style="width:80%;height:35;font-size:15;visibility: hidden;"></div>',
        //lessLink: '<a href="#">Close</a>',
        lessLink: '<div><input type="button" value="Close" style="width:80%;height:35;font-size:15;"></div>',
        embedCSS: true,
        sectionCSS: 'display: block; width: 100%;',

        beforeToggle: function(){},
        afterToggle: function(){}
      },
      cssEmbedded = false;

  function Showmore( element, options ) {
    this.element = element;

    this.options = $.extend( {}, defaults, options);

    $(this.element).data('max-height', this.options.maxHeight);

    delete(this.options.maxHeight);

    if(this.options.embedCSS && ! cssEmbedded) {
      var styles = '.showmore-js-toggle, .showmore-js-section { ' + this.options.sectionCSS + ' } .showmore-js-section { overflow: hidden; }';

      (function(d,u) {
        var css=d.createElement('style');
        css.type = 'text/css';
        if(css.styleSheet) {
            css.styleSheet.cssText = u;
        }
        else {
            css.appendChild(d.createTextNode(u));
        }
        d.getElementsByTagName("head")[0].appendChild(css);
      }(document, styles));

      cssEmbedded = true;
    }

    this._defaults = defaults;
    this._name = showmore;

    this.init();
  }

  Showmore.prototype = {

    init: function() {
      var $this = this;

      $(this.element).each(function() {
        var current = $(this),
            maxHeight = (current.css('max-height').replace(/[^-\d\.]/g, '') > current.data('max-height')) ? current.css('max-height').replace(/[^-\d\.]/g, '') : current.data('max-height');

        current.addClass('showmore-js-section');

        if(current.css('max-height') != "none") {
          current.css("max-height", "none");
        }

        current.data("boxHeight", current.outerHeight(true));

        if(current.outerHeight(true) < maxHeight) {
          return true;
        }
        else {
          current.after($($this.options.moreLink).on('click', function(event) { $this.toggleSlider(this, current, event) }).addClass('showmore-js-toggle'));
        }

        current.data('sliderHeight', maxHeight);

        current.css({height: maxHeight});
      });
    },

    toggleSlider: function(trigger, element, event)
    {
      event.preventDefault();

      var $this = this,
          newHeight = newLink = '',
          more = false,
          sliderHeight = $(element).data('sliderHeight');

	  //if(xxx==1)	firsth = $(element).data().boxHeight;
      //if ($(element).height() == sliderHeight) {
      //if ($(element).height() <= $(element).data().boxHeight) {
        //newHeight = $(element).data().boxHeight + "px";
        //newHeight = 100*xxx;
//        newHeight = sliderHeight*xxx;
        //newLink = 'lessLink';
        
//        newHeight = $(element).height()+sliderHeight;
        //newHeight = $(element).height();
        //newHeight = $(element).data().boxHeight + sliderHeight;
        
        //if(newHeight > $(element).data().boxHeight)
       	//			newHeight = sliderHeight;
     
     
     	//何pxずつ： $this.options.speed
     
        //最初の高さ: sliderHeight   
        //現在の高さ: $(element).height()
        //最大の高さ: $(element).data().boxHeight
        
        var px = $this.options.showEach;
		if(px>0){
	        if($(element).height()==$(element).data().boxHeight)
	        	newHeight = sliderHeight;
	        else
	        	newHeight = $(element).height() + px;	//newHeight = $(element).height() + sliderHeight;
	        
	        if(newHeight<=$(element).data().boxHeight){
	        //if((sliderHeight*(xxx+1))<$(element).data().boxHeight){
	        //if ($(element).height() > sliderHeight) {
	       // 	if((sliderHeight*(xxx+1))<=$(element).data().boxHeight){
	       			//newHeight = $(element).height() + sliderHeight;
	//       			newHeight = sliderHeight;
	       //			if( (newHeight + sliderHeight) > $(element).data().boxHeight)
	       //				newHeight = sliderHeight;
	       			
	        	newLink = 'moreLink';
	        }else{
	        		
	        	newHeight = $(element).data().boxHeight + "px";
	        		//newHeight = $(element).data().height() + "px";
	        		//newHeight = sliderHeight + "px";
	       		newLink = 'lessLink';
	       		more = true;
	        }
        }else{
	      	if ($(element).height() == sliderHeight) {
	        	newHeight = $(element).data().boxHeight + "px";
	        	newLink = 'lessLink';
	        	more = true;
	      	}else {
	        	newHeight = sliderHeight;
	        	newLink = 'moreLink';
	      	}
        }


      $this.options.beforeToggle(trigger, element, more);

      $(element).animate({"height": newHeight}, {duration: $this.options.speed });

      $(trigger).replaceWith($($this.options[newLink]).on('click', function(event) { $this.toggleSlider(this, element, event) }).addClass('showmore-js-toggle'));

      $this.options.afterToggle(trigger, element, more);
    }
  };

  $.fn[showmore] = function( options ) {
    var args = arguments;
    if (options === undefined || typeof options === 'object') {
      return this.each(function () {
        if (!$.data(this, 'plugin_' + showmore)) {
          $.data(this, 'plugin_' + showmore, new Showmore( this, options ));
        }
      });
    } else if (typeof options === 'string' && options[0] !== '_' && options !== 'init') {
      return this.each(function () {
        var instance = $.data(this, 'plugin_' + showmore);
        if (instance instanceof Showmore && typeof instance[options] === 'function') {
          instance[options].apply( instance, Array.prototype.slice.call( args, 1 ) );
        }
      });
    }
  }
})(jQuery);