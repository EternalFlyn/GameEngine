#version 450 core

layout (location = 0) in vec4 position;

layout (location = 0) out vec4 color;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;

void main() {
	gl_Position = projection * view * transformation * position;
	color = vec4(position.x + 0.5, 1.0, position.y + 0.5, 1.0);
}