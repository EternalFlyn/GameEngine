#version 450 core

layout (location = 0) in vec4 vertices;

layout (location = 0) out vec4 color;

void main() {
	gl_Position = vertices;
	color = vec4(vertices.x + 0.5, 1.0, vertices.y + 0.5, 1.0);
}