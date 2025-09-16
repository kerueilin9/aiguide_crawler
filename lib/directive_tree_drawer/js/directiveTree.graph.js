function update(source) {
  // Compute the new tree layout.
  var nodes = tree.nodes(root).reverse(),
	    links = tree.links(nodes);
  var  treeDirection = "horizontal";
  // Normalize for fixed-depth.
  nodes.forEach(function(d) { d.y = d.depth * 180; });

  // Update the nodes??
  var node = svg.selectAll("g.node")
	  .data(nodes, function(d) { return d.id || (d.id = ++i); });

  // Enter any new nodes at the parent's previous position.
  var nodeEnter = node.enter().append("g")
	  .attr("class", "node")
	  .attr("transform", function(d) { return "translate(" + source.y0 + "," + source.x0 + ")"; })
	  .on("click", click);

  setNodeContent(nodeEnter);
  
  // Transition nodes to their new position.
  var nodeUpdate = node.transition()
	  .duration(duration)
	  .attr("transform", function(d) { 
      var translate = "translate(" + d.y + "," + d.x + ")"; 
      if (treeDirection == "vertical") translate = "translate(" + d.x + "," + d.y + ")"; 
      return translate;
      });

  setNodeNewForm(nodeUpdate)


  // Transition exiting nodes to the parent's new position.
  var nodeExit = node.exit().transition()
	  .duration(duration)
	  .attr("transform", function(d) { return "translate(" + source.y + "," + source.x + ")"; })
	  .remove();
    
  setNodeExistForm(nodeExit)

  // Update the links??
  var link = svg.selectAll("path.link")
	  .data(links, function(d) { return d.target.id; });

  // Enter any new links at the parent's previous position.
  link.enter().insert("path", "g")
	  .attr("class", "link")
	  .attr("d", function(d) {
		var o = {x: source.x0, y: source.y0};
		return diagonal({source: o, target: o});
	  });

  // Transition links to their new position.
  link.transition()
	  .duration(duration)
	  .attr("d", diagonal);

  // Transition exiting nodes to the parent's new position.
  link.exit().transition()
	  .duration(duration)
	  .attr("d", function(d) {
		var o = {x: source.x, y: source.y};
		return diagonal({source: o, target: o});
	  })
	  .remove();

  // Stash the old positions for transition.
  nodes.forEach(function(d) {
	d.x0 = d.x;
	d.y0 = d.y;
  });
}

// graph setting
function getDirectiveTreeDepth(tree){
  var depth = 0;
  var childNode = tree.children;
  if (childNode.length == 0) depth = 1;
  else{
    var max = -1;
    for(var i = 0; i < childNode.length; i++){
      max = Math.max(max, getDirectiveTreeDepth(childNode[i]));
    }
    depth += max + 1;
  }
  return depth
}

function getDirectiveTreeBreadth(tree){
  var depth = 0;
  var childNode = tree.children;
  if (childNode.length == 0) depth = 1;
  else{
    for(var i = 0; i < childNode.length; i++){
      depth += getDirectiveTreeBreadth(childNode[i]);
    }
  }
  return depth
}


// set Note content 
function setNodeContent(nodeEnter){
  var circleRadius = 10;
  nodeEnter.append("circle")
	  .attr("r", circleRadius)
    .style("fill", function(d) { return d._children ? "lightsteelblue" : "#fff"; });

  console.log(nodeEnter);

  var textSize = 12;
  nodeEnter.append("text")
	  .attr("x", function(d) { return d.children || d._children || d.Type=="InputPage" ? -20 : 20; })
	  .attr("dy", function(d) {
      if (d.children || d._children || d.Type=="InputPage"){
        return circleRadius*2 + 6
      }else{
        return d.Type=="Directive" ? (circleRadius*2 - textSize) / 2 : ( circleRadius*2 - (textSize * 2 + 6))/2
      }
    })
	.attr("text-anchor", "start")
	.text(function(d) {
      if (d.Type == "Directive"){
        var directive_tree_class_id = d.id
        var directive_tree_id = directive_tree_class_id.substr(directive_tree_class_id.indexOf("@") + 1)
        return "Directive ID: " + directive_tree_id;
      }
      if (d.Type == "InputPage") return "InputPage ID: " + d.stateID
    })
    .style("fill-opacity", 1e-6)
    .append("tspan")
    .attr("x", function(d) { return d.children || d._children || d.Type=="InputPage" ? -20 : 20; })
    .attr("dy", function(d) { return textSize + 6})
    .text(
      function(d) {
        if (d.Type == "InputPage") {
          var urlWithoutHttp = d.targetURL.substr("https://".length)
          return "InputPage URL: " + urlWithoutHttp.substr(urlWithoutHttp.indexOf("/"))
        } else if (d.Type == "Directive") {
          return "Form XPath: " + d.formXPath
        }
     });
}

function setNodeNewForm(nodeUpdate){
  nodeUpdate.select("circle")
	  .attr("r", 10)
	  .style("fill", function(d) { 
      var color = "black";
      if (d.Type == "Directive"){
        color = "red"
      }
      if(d.Type == "InputPage"){
        color = "#FFF"
      }
      return color
     });

  nodeUpdate.select("text")
	  .style("fill-opacity", 1);
}

function setNodeExistForm(nodeExit){
  nodeExit.select("circle")
	  .attr("r", 1e-6);

  nodeExit.select("text")
	  .style("fill-opacity", 1e-6);
}


// show directive action sequence click.
function click(d) {
  showModal(d)
}