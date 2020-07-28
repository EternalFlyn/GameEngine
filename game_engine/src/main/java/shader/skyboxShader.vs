#version 450 core

in vec3 position;

out vec3 coords;

uniform mat4 projection;
uniform mat4 view;

void main(void) {
	gl_Position = projection * view * vec4(position, 1);
	coords = position;
}