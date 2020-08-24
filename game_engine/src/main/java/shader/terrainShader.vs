#version 450 core

in vec4 position;
in vec2 texturedCoords;
in vec3 normal;

out vec2 coords;
out vec3 surfaceNormal;
out vec3 toLightVector[4];
out vec3 toCameraVector;
out float visibility;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;
uniform vec3 lightPosition[4];
uniform vec4 clipPlane;

const float fogDensity = 0.01;
const float forGradient = 1.5;

void main() {
	vec4 worldPosition = transformation * position;
	
	gl_ClipDistance[0] = dot(worldPosition, clipPlane);
	
	vec4 positionToCamera = view * worldPosition;
	gl_Position = projection * positionToCamera;
	coords = texturedCoords;
	
	surfaceNormal = (transformation * vec4(normal, 0)).xyz;
	for(int i = 0; i < 4; i++) toLightVector[i] = lightPosition[i] - worldPosition.xyz;
	toCameraVector = (inverse(view) * vec4(0, 0, 0, 1)).xyz - worldPosition.xyz;
	
	float distance = length(positionToCamera.xyz);
	visibility = exp(-pow((distance * fogDensity), forGradient));
	visibility = clamp(visibility, 0, 1);
}