#version 450 core

layout (location = 0) in vec4 position;
layout (location = 1) in vec2 texturedCoords;

layout (location = 0) out vec2 coords;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;

void main() {
	gl_Position = projection * view * transformation * position;
	coords = texturedCoords;
}