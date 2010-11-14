package org.rascalmpl.library.vis.graph.spring;

import java.util.LinkedList;

import org.rascalmpl.library.vis.Figure;

import processing.core.PApplet;

/**
 * A SpringGraphNode is created for each "node" constructor that occurs in the graph.
 * 
 * @author paulk
 *
 */
public class SpringGraphNode {
	
	private SpringGraph G;
	protected String name;
	protected Figure figure;
	protected float x;
	protected float y;

	protected float dispx = 0f;
	protected float dispy = 0f;
	
	protected LinkedList<SpringGraphNode> in;
	protected LinkedList<SpringGraphNode> out;
	private static boolean debug = false;
	
	SpringGraphNode(SpringGraph springGraph, String name, Figure fig){
		this.G = springGraph;
		this.name = name;
		this.figure = fig;
		in = new LinkedList<SpringGraphNode>();
		out = new LinkedList<SpringGraphNode>();
	}
	
	public void addIn(SpringGraphNode n){
		if(!in.contains(n))
			in.add(n);
	}
	
	public void addOut(SpringGraphNode n){
		if(!out.contains(n))
			out.add(n);
	}
	
	public float xdistance(SpringGraphNode other){
		float vx = x - other.x;
		return vx;
//		if(vx > 0){
//			return PApplet.max(vx - (velem.width/2 + other.velem.width/2), 0.01f);
//		} else {
//			return PApplet.min(vx + (velem.width/2 + other.velem.width/2), -0.01f);
//		}
	}
	
	public float ydistance(SpringGraphNode other){
		float vy = y - other.y ;
		return vy;
//		if(vy > 0){
//			return PApplet.max(vy - (velem.height/2 + other.velem.height/2), 0.01f);
//		} else {
//			return PApplet.min(vy + (velem.height/2 + other.velem.height/2), -0.01f);
//		}
	}
	
//	public float getMass(){
//		return 1.0f;
//	}
	
	private void repulsion(float vx, float vy){
		// Inline version of repel(d) = SpringCon^2/d
		
		float dlensq = vx * vx + vy * vy;
		
		if(PApplet.abs(dlensq) < 1){
			dlensq = dlensq < 0 ? -0.01f : 0.01f;
			float r1 = (float) Math.random();
			float r2 = (float) Math.random();
			
			vx = vx > 0 ? vx + r1 : vx - r1;
			vy = vy > 0 ? vy + r2 : vy - r2;
		}
		
		dispx += vx * G.springConstant2 / dlensq;
		dispy += vy * G.springConstant2 / dlensq;
	}
	
	public void relax(){
		
		dispx = dispy = 0;
		
		for(SpringGraphNode n : G.nodes){
			if(n != this){
				repulsion(xdistance(n), ydistance(n));
			}
		}
		
		// Consider the repulsion of the 4 walls of the surrounding frame
		repulsion(x, G.height/2); repulsion(G.width - x, G.height/2);
		repulsion(G.width/2, y); repulsion(G.width/2, G.height - y);
		
		
//		for(GraphEdge e : G.edges){
//			GraphNode from = e.from;
//			GraphNode to = e.to;
//			if(from != this && to != this){
//				float vlen = PApplet.dist(from.x, from.y, to.x, to.y);
//				float lenToFrom = PApplet.dist(x, y, from.x, from.y);
//				float lenToTo = PApplet.dist(x, y, to.x, to.y);
//				if(lenToFrom + lenToTo - vlen < 1f){
//					dispx += 1;
//					dispy += 1;
//					from.dispx -= 1;
//					from.dispy -= 1;
//					to.dispx -= 1;
//					to.dispy -= 1;
//				}
//			}
//		}
		
		if(debug)System.err.printf("Node %s (%f,%f), dispx = %f, dispy =%f\n", name, x, y, dispx, dispy);
	}
	
	void update(SpringGraph G){
		float dlen = PApplet.mag(dispx, dispy);
		if(dlen > 0){
			if(debug)System.err.printf("update %s, dispx=%f, dispy=%f, from %f, %f -> ", name, dispx, dispy, x, y);
			x += PApplet.constrain(dispx, -G.temperature, G.temperature);
			y += PApplet.constrain(dispy, -G.temperature, G.temperature);
			//x =  PApplet.constrain (x, velem.width/2, G.width-velem.width/2);
			//y =  PApplet.constrain (y, velem.height/2, G.height-velem.height/2);
			if(debug)System.err.printf("%f, %f\n", x, y);
		}
	}
	
	public float figX(){
		return x;
	}
	
	public float figY(){
		return y;
	}
	
	void bbox(){
		if(figure != null){
			figure.bbox();
		}
	}
	
	float width(){
		return figure != null ? figure.width : 0;
	}
	
	float height(){
		return figure != null ? figure.height : 0;
	}

	void draw(float left, float top) {
		if(figure != null){
			figure.bbox();
			figure.draw(x + left - figure.width/2, y + top - figure.height/2);
		}
	}
	
	public boolean mouseOver(int mousex, int mousey){
		if(figure.mouseInside(mousex, mousey)){
			figure.fpa.registerFocus(figure);
			return true;
		}
		return false;
	}
	
	public boolean mousePressed(int mousex, int mousey){
		if(figure.mouseInside(mousex, mousey)){
			figure.fpa.registerFocus(figure);
			return true;
		}
		return false;
	}
}
