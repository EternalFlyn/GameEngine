#version 450 core

in vec3 position;

out vec4 color;

uniform mat4 projection;
uniform mat4 view;

void main(void) {
	gl_Position = projection * view * vec4(position, 1);
	color = vec4(1, 1, 1, 1);
}