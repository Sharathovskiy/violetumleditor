/*
 Violet - A program for editing UML diagrams.

 Copyright (C) 2007 Cay S. Horstmann (http://horstmann.com)
 Alexandre de Pellegrin (http://alexdp.free.fr);

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.horstmann.violet.product.diagram.sequence.nodes;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.type.NullType;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideCustomShape;
import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.AbstractNode;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.sequence.edges.CallEdge;
import com.horstmann.violet.product.diagram.sequence.edges.ReturnEdge;

/**
 * An activation bar in a sequence diagram. This activation bar is hang on a lifeline (implicit parameter)
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 */
public class ActivationBarNode extends ColorableNode
{
    protected static class ActivationBarShape implements ContentInsideCustomShape.ShapeCreator
    {
        @Override
        public Shape createShape(int contentWidth, int contentHeight)
        {
            return new Rectangle2D.Double(0,0, DEFAULT_WIDTH, contentHeight);
        }
    }

    public ActivationBarNode()
    {
        super();
        createContentStructure();
    }

    protected ActivationBarNode(ActivationBarNode node) throws CloneNotSupportedException
    {
        super(node);
        createContentStructure();
    }

    @Override
    public void deserializeSupport()
    {
        super.deserializeSupport();
        for(INode child : getChildren())
        {
            if (child instanceof ActivationBarNode)
            {
                activationsGroup.add(((ActivationBarNode) child).getContent());
            }
        }
    }

    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new ActivationBarNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        activationsGroup = new RelativeLayout();
        activationsGroup.setMinHeight(DEFAULT_HEIGHT);
        activationsGroup.setMinWidth(DEFAULT_WIDTH);

        EmptyContent padding = new EmptyContent();
        padding.setMinHeight(DEFAULT_CHILD_VERTICAL_MARGIN);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(padding);
        verticalLayout.add(activationsGroup);
        verticalLayout.add(padding);

        ContentInsideShape contentInsideShape = new ContentInsideCustomShape(verticalLayout, new ActivationBarShape());

        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));
        setContent(getBackground());

        setTextColor(getTextColor());
    }

    @Override
    public void removeChild(INode node)
    {
        activationsGroup.remove(((ActivationBarNode) node).getContent());
        super.removeChild(node);
    }

    @Override
    public boolean addChild(INode node, Point2D point)
    {
        if (! (node instanceof ActivationBarNode))
        {
            return false;
        }

        addChild(node, getChildren().size());

        ActivationBarNode activationBarNode = (ActivationBarNode) node;
        activationBarNode.setTextColor(getTextColor());
        activationBarNode.setBackgroundColor(getBackgroundColor());
        activationBarNode.setBorderColor(getBorderColor());

        activationsGroup.add(activationBarNode.getContent());

        activationBarNode.setLocation(point);
        activationBarNode.setGraph(getGraph());
        activationBarNode.setParent(this);

        return true;
    }

    protected void onChildChangeLocation(INode child)
    {
        activationsGroup.setPosition(((AbstractNode) child).getContent(), getChildRelativeLocation(child));
    }

    protected Point2D getChildRelativeLocation(INode node)
    {
        Point2D nodeLocation = node.getLocation();
        if(DEFAULT_CHILD_VERTICAL_MARGIN > nodeLocation.getY() || DEFAULT_CHILD_LEFT_MARGIN != nodeLocation.getX())
        {
            nodeLocation.setLocation(DEFAULT_CHILD_LEFT_MARGIN, Math.max(nodeLocation.getY(), DEFAULT_CHILD_VERTICAL_MARGIN));
            node.setLocation(nodeLocation);
        }

        return new Point2D.Double(nodeLocation.getX()+DEFAULT_CHILD_LEFT_MARGIN, nodeLocation.getY()- DEFAULT_CHILD_VERTICAL_MARGIN);
    }

    @Override
    public boolean addConnection(IEdge edge)
    {
        INode endingNode = edge.getEnd();
        INode startingNode = edge.getStart();

        if(null == endingNode)
        {
            return false;
        }
        if (startingNode instanceof ActivationBarNode && endingNode instanceof ActivationBarNode)
        {
            if(startingNode.getParents().get(0) == endingNode.getParents().get(0))
            {
                if (edge instanceof CallEdge)
                {
                    return (startingNode == endingNode.getParent());
                }
            }
        }
        if (edge instanceof CallEdge)
        {
            return isCallEdgeAcceptable((CallEdge) edge);
        }
        if (edge instanceof ReturnEdge)
        {
            return isReturnEdgeAcceptable((ReturnEdge) edge);
        }
        return false;
    }

    @Override
    public Point2D getConnectionPoint(IEdge edge)
    {
        Direction edgeDirection = edge.getDirection(this);
        Point2D startingNodeLocation = getLocationOnGraph();

        double x = startingNodeLocation.getX();
        double y = startingNodeLocation.getY();

        if (0 >= edgeDirection.getX())
        {
            x+=DEFAULT_WIDTH;
        }

        if(edge instanceof CallEdge)
        {
            if (edge.getEnd() instanceof LifelineNode)
            {
                y += CALL_YGAP / 2;
            }
            else if (null != edge.getStart().getParent() &&
                     null != edge.getEnd().getParent() &&
                     edge.getStart().getParents().get(0) == edge.getEnd().getParents().get(0))
            {
                if (0 < edgeDirection.getX())
                {
                    x += DEFAULT_WIDTH;
                }
                if(this == edge.getStart())
                {
                    y += edge.getEnd().getLocation().getY() - CALL_YGAP/2;
                }
            }
            else if(this == edge.getStart())
            {
                y = edge.getEnd().getLocationOnGraph().getY();
            }
        }
        else if(edge instanceof ReturnEdge)
        {
            if(this == edge.getStart())
            {
                y += getContent().getHeight();
            }
            else if(this == edge.getEnd())
            {
                y = edge.getStart().getLocationOnGraph().getY() + edge.getStart().getBounds().getHeight();
            }
        }
        return new Point2D.Double(x, y);
    }

    /**
     * 
     * @return true if this activation bar has been called by another activation bar
     */
    private boolean isCalledNode()
    {
        LifelineNode currentLifelineNode = getImplicitParameter();
        for (IEdge edge : getGraph().getAllEdges())
        {
            if (edge.getEnd() != this)
            {
                continue;
            }
            if (!edge.getClass().isAssignableFrom(CallEdge.class))
            {
                continue;
            }
            INode startingNode = edge.getStart();
            if (!startingNode.getClass().isAssignableFrom(ActivationBarNode.class))
            {
                continue;
            }
            if (((ActivationBarNode) startingNode).getImplicitParameter() == currentLifelineNode)
            {
                continue;
            }
            return true;
        }
        return false;
    }

    @Override
    public Rectangle2D getBounds()
    {
        refreshPositionAndSize();

        return super.getBounds();
    }

    private void refreshPositionAndSize()
    {
        setLocation(calculateLocation());
        activationsGroup.setMinHeight((int)Math.max(calculateHeight(), DEFAULT_HEIGHT ));
    }

    private Point2D calculateLocation()
    {
        double y = this.getLocation().getY();

        for (IEdge edge : getGraph().getAllEdges())
        {
            if (edge instanceof CallEdge)
            {
                if (edge.getStart() == this && edge.getStart() != getParent())
                {
                    y = Math.min(y, edge.getEnd().getLocationOnGraph().getY()-5);
                }
            }
        }
        return new Point.Double(this.getLocation().getX(), y);
    }

    private double calculateHeight()
    {
        double height = 0;
        for (IEdge edge : getGraph().getAllEdges())
        {
            if (edge instanceof CallEdge)
            {
                if (edge.getStart() == this && edge.getStart() != getParent())
                {
                    INode endingNode = edge.getEnd();
                    if (endingNode instanceof ActivationBarNode)
                    {
                        Rectangle2D endingNodeBounds = endingNode.getBounds();
                        double newHeight = endingNodeBounds.getHeight() + (endingNode.getLocationOnGraph().getY() - this.getLocationOnGraph().getY());
                        height = Math.max(height, newHeight);
                    }
                }
            }
        }
        return Math.max(DEFAULT_HEIGHT, height);
    }


    @Override
    public Point2D getLocation()
    {
        return super.getLocation();

//        if (this.locationCache != null) {
//        	return this.locationCache;
//        }
//    	INode parentNode = getParent();
//        if (parentNode == null) {
//        	this.locationCache = super.getLocation();
//            return this.locationCache;
//        }
//        List<IEdge> connectedEdges = getConnectedEdges();
//        boolean isChildOfActivationBarNode = (parentNode.getClass().isAssignableFrom(ActivationBarNode.class));
//        boolean isChildOfLifelineNode = (parentNode.getClass().isAssignableFrom(LifelineNode.class));
//        // Case 1 : just attached to a lifeline
//        if (isChildOfLifelineNode && connectedEdges.isEmpty()) {
//            Point2D rawLocation = super.getLocation();
//            double horizontalLocation = getHorizontalLocation();
//            double verticalLocation = rawLocation.getY();
//            Point2D adjustedLocation = new Point2D.Double(horizontalLocation, verticalLocation);
//            adjustedLocation = getGraph().getGridSticker().snap(adjustedLocation);
//            super.setLocation(adjustedLocation);
//            this.locationCache = adjustedLocation;
//            return adjustedLocation;
//        }
//        // Case 2 : is child of another activation bar
//        if (isChildOfActivationBarNode && connectedEdges.isEmpty()) {
//            Point2D rawLocation = super.getLocation();
//            double horizontalLocation = getHorizontalLocation();
//            double verticalLocation = rawLocation.getY();
//            verticalLocation = Math.max(verticalLocation, CALL_YGAP);
//            Point2D adjustedLocation = new Point2D.Double(horizontalLocation, verticalLocation);
//            adjustedLocation = getGraph().getGridSticker().snap(adjustedLocation);
//            super.setLocation(adjustedLocation);
//            this.locationCache = adjustedLocation;
//            return adjustedLocation;
//        }
//        // Case 3 : is connected
//        if (!connectedEdges.isEmpty()) {
//            Point2D rawLocation = super.getLocation();
//            for (IEdge edge : getConnectedEdges()) {
//                if (!edge.getClass().isAssignableFrom(CallEdge.class))
//                {
//                    continue;
//                }
//                if (edge.getEnd() == this)
//                {
//                    INode startingNode = edge.getStart();
//                    Point2D startingNodeLocationOnGraph = startingNode.getLocationOnGraph();
//                    Point2D endingNodeParentLocationOnGraph = getParent().getLocationOnGraph();
//                    double yGap = rawLocation.getY() - startingNodeLocationOnGraph.getY() + endingNodeParentLocationOnGraph.getY();
//                    if (yGap < CALL_YGAP / 2) {
//                        double horizontalLocation = getHorizontalLocation();
//                        double minY = startingNodeLocationOnGraph.getY() - endingNodeParentLocationOnGraph.getY() + CALL_YGAP / 2;
//                        Point2D adjustedLocation = new Point2D.Double(horizontalLocation, minY);
//                        adjustedLocation = getGraph().getGridSticker().snap(adjustedLocation);
//                        super.setLocation(adjustedLocation);
//                        this.locationCache = adjustedLocation;
//                        return adjustedLocation;
//                    }
//                    break;
//                }
//            }
//        }
//        // Case 4 : default case
//        Point2D rawLocation = super.getLocation();
//        double horizontalLocation = getHorizontalLocation();
//        double verticalLocation = rawLocation.getY();
//        Point2D adjustedLocation = new Point2D.Double(horizontalLocation, verticalLocation);
//        adjustedLocation = getGraph().getGridSticker().snap(adjustedLocation);
//        super.setLocation(adjustedLocation);
//        this.locationCache = adjustedLocation;
//        return adjustedLocation;
    }

    /**
     * Gets the participant's life line of this call. Note : method's name is ot set to getLifeLine to keep compatibility with older
     * versions
     * 
     * @return the participant's life line
     */
    public LifelineNode getImplicitParameter()
    {
        if (this.lifeline == null) {
        	INode aParent = this.getParent();
        	List<INode> nodeStack = new ArrayList<INode>();
        	nodeStack.add(aParent);
        	while (!nodeStack.isEmpty()) {
        		INode aNode = nodeStack.get(0);
        		if (LifelineNode.class.isInstance(aNode)) {
        			this.lifeline = (LifelineNode) aNode;
        		}
        		if (ActivationBarNode.class.isInstance(aNode)) {
        			INode aNodeParent = aNode.getParent();
        			nodeStack.add(aNodeParent);
        		}
        		nodeStack.remove(0);
        	}
        }
    	return lifeline;
    }

    /**
     * Sets the participant's life line of this call. Note : method's name is ot set to setLifeLine to keep compatibility with older
     * versions
     * 
     * @param newValue the participant's lifeline
     */
    public void setImplicitParameter(LifelineNode newValue)
    {
        // Nothing to do. Just here to keep compatibility.
    }

    private boolean isReturnEdgeAcceptable(ReturnEdge edge)
    {
        if(!(edge.getEnd() instanceof ActivationBarNode && edge.getStart() instanceof ActivationBarNode))
        {
            return false;
        }

        ActivationBarNode startingActivationBarNode = (ActivationBarNode) edge.getStart();
        ActivationBarNode endingActivationBarNode = (ActivationBarNode) edge.getEnd();
        if (startingActivationBarNode.getImplicitParameter() == endingActivationBarNode.getImplicitParameter())
        {
            return false;
        }
        if (!isCalledNode())
        {
            return false;
        }
        return true;
    }

    private boolean isCallEdgeAcceptable(CallEdge edge)
    {
        INode endingNode = edge.getEnd();
        INode startingNode = edge.getStart();




        Point2D endingNodePoint = edge.getEndLocation();
        Class<?> startingNodeClass = (startingNode != null ? startingNode.getClass() : NullType.class);
        Class<?> endingNodeClass = (endingNode != null ? endingNode.getClass() : NullType.class);
        // Case 1 : check is call edge doesn't connect already connected nodes
        if (startingNode != null && endingNode != null) {
        	for (IEdge anEdge : getGraph().getAllEdges()) {
        		if (!anEdge.getClass().isAssignableFrom(CallEdge.class)) {
        			continue;
        		}
        		INode anEdgeStartingNode = anEdge.getStart();
        		INode anEdgeEndingNode = anEdge.getEnd();
        		if (startingNode.equals(anEdgeStartingNode) && endingNode.equals(anEdgeEndingNode)) {
        			return false;
        		}
        	}
        }
        // Case 2 : classic connection between activation bars
        if (startingNodeClass.isAssignableFrom(ActivationBarNode.class)
                && endingNodeClass.isAssignableFrom(ActivationBarNode.class))
        {
            return true;
        }
        // Case 3 : an activation bar creates a new class instance
        if (startingNodeClass.isAssignableFrom(ActivationBarNode.class) && endingNodeClass.isAssignableFrom(LifelineNode.class))
        {
            ActivationBarNode startingActivationBarNode = (ActivationBarNode) startingNode;
            LifelineNode startingLifeLineNode = startingActivationBarNode.getImplicitParameter();
            LifelineNode endingLifeLineNode = (LifelineNode) endingNode;
            Rectangle2D topRectangle = endingLifeLineNode.getTopRectangle();
            if (startingLifeLineNode != endingLifeLineNode && topRectangle.contains(endingNodePoint))
            {
                return true;
            }
        }
        // Case 4 : classic connection between activation bars but the ending
        // bar doesn't exist and need to be automatically created
        if (startingNodeClass.isAssignableFrom(ActivationBarNode.class) && endingNodeClass.isAssignableFrom(LifelineNode.class))
        {
            ActivationBarNode startingActivationBarNode = (ActivationBarNode) startingNode;
            LifelineNode startingLifeLineNode = startingActivationBarNode.getImplicitParameter();
            LifelineNode endingLifeLineNode = (LifelineNode) endingNode;
            Rectangle2D topRectangle = endingLifeLineNode.getTopRectangle();
            if (startingLifeLineNode != endingLifeLineNode && !topRectangle.contains(endingNodePoint))
            {
                ActivationBarNode newActivationBar = new ActivationBarNode();
                Point2D edgeStartLocation = edge.getEndLocation();
                double x = edgeStartLocation.getX();
                double y = edgeStartLocation.getY();
                Point2D newActivationBarLocation = new Point2D.Double(x, y);
                endingNode.addChild(newActivationBar, newActivationBarLocation);
                edge.setEnd(newActivationBar);
                return true;
            }
        }
        // Case 5 : self call on an activation bar
        if (startingNodeClass.isAssignableFrom(ActivationBarNode.class) && endingNodeClass.isAssignableFrom(LifelineNode.class))
        {
            ActivationBarNode startingActivationBarNode = (ActivationBarNode) startingNode;
            LifelineNode startingLifeLineNode = startingActivationBarNode.getImplicitParameter();
            LifelineNode endingLifeLineNode = (LifelineNode) endingNode;
            Rectangle2D topRectangle = endingLifeLineNode.getTopRectangle();
            if (startingLifeLineNode == endingLifeLineNode && !topRectangle.contains(endingNodePoint))
            {
                ActivationBarNode newActivationBar = new ActivationBarNode();
                Point2D edgeStartLocation = edge.getStartLocation();
                double x = edgeStartLocation.getX();
                double y = edgeStartLocation.getY() + CALL_YGAP / 2;
                Point2D newActivationBarLocation = new Point2D.Double(x, y);
                startingNode.addChild(newActivationBar, newActivationBarLocation);
                edge.setEnd(newActivationBar);
                return true;
            }
        }
        // Case 6 : self call on an activation bar but its child which is called
        // doesn"t exist and need to be created automatically
        if (startingNodeClass.isAssignableFrom(ActivationBarNode.class) && endingNodeClass.isAssignableFrom(NullType.class))
        {
            ActivationBarNode newActivationBar = new ActivationBarNode();
            edge.getStartLocation();
            startingNode.addChild(newActivationBar, edge.getStartLocation());
            edge.setEnd(newActivationBar);
            return true;
        }
        return false;
    }

    /**
     * Finds an edge in the graph connected to start and end nodes
     * 
     * @param start the start node_old
     * @param end the end node_old
     * @return the edge or null if no one is found
     */
    private IEdge findEdge(INode start, INode end)
    {
        for (IEdge e : getGraph().getAllEdges())
        {
            if (e.getStart() == start && e.getEnd() == end) return e;
        }
        return null;
    }




    /** The lifeline that embeds this activation bar in the sequence diagram */
    private transient LifelineNode lifeline; //TODO remove

    private transient RelativeLayout activationsGroup = null;
    
    /** Default with */
    public final static int DEFAULT_WIDTH = 16;
    public final static int DEFAULT_CHILD_LEFT_MARGIN = 5;
    public final static int DEFAULT_CHILD_VERTICAL_MARGIN = 10;

    /** Default height */
    private final static int DEFAULT_HEIGHT = 20;

    /** Default vertical gap between two call nodes and a call node_old and an implicit node_old */
    public final static int CALL_YGAP = 20;
}
