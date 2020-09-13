#version 450 core

in vec2 position;

out vec2 coords;

uniform mat4 transformation;

void main(void) {
	gl_Position = transformation * vec4(position, 0, 1);
	coords = vec2(position.x / 2, -position.y / 2);
}