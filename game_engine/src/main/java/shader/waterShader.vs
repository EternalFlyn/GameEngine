#version 450 core

in vec2 position;

out vec2 coords;
out vec3 toCameraVector;
out vec3 formLightVector;
out vec4 clipSpace;

uniform vec3 cameraPosition;
uniform vec3 lightPosition;
uniform mat4 projection;
uniform mat4 view;
uniform mat4 transformation;

void main(void) {

	vec4 worldPosition = transformation * vec4(position.x, 0.0, position.y, 1.0);
	clipSpace = projection * view * worldPosition;
	gl_Position = clipSpace;
	coords = vec2(position.x / 2 + 0.5, position.y / 2 + 0.5);
	toCameraVector = cameraPosition - worldPosition.xyz;
	formLightVector = worldPosition.xyz - lightPosition;
}