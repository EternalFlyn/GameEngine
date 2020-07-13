#version 450 core

layout (location = 0) in vec4 position;
layout (location = 1) in vec2 texturedCoords;
layout (location = 2) in vec3 normal;

layout (location = 0) out vec2 coords;
layout (location = 1) out vec3 surfaceNormal;
layout (location = 2) out vec3 toLightVector;
layout (location = 3) out vec3 toCameraVector;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;
uniform vec3 lightPosition;
uniform float useFakeLight;

void main() {
	vec4 worldPosition = transformation * position;
	gl_Position = projection * view * worldPosition;
	coords = texturedCoords;
	
	vec3 actualNormal = normal;
	if(useFakeLight > 0.5) {
		actualNormal = vec3(0, 1, 0);
	}
	
	surfaceNormal = (transformation * vec4(actualNormal, 0)).xyz;
	toLightVector = lightPosition - worldPosition.xyz;
	toCameraVector = (inverse(view) * vec4(0, 0, 0, 1)).xyz - worldPosition.xyz;
}