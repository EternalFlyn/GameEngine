#version 450 core

layout (location = 0) in vec4 position;
layout (location = 1) in vec2 texturedCoords;

layout (location = 0) out vec2 coords;

void main() {
	gl_Position = position;
	coords = texturedCoords;
}