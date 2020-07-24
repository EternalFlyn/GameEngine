#version 450 core

layout (location = 0) in vec2 position;

layout (location = 0) out vec2 coords;

uniform mat4 transformation;

void main(void) {
	gl_Position = transformation * vec4(position, 0, 1);
	coords = vec2(position.x / 2, -position.y / 2);
}