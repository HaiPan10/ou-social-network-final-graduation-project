import Particles from "react-tsparticles";
import { loadSlim } from "tsparticles-slim";
import { useCallback, useMemo } from "react";


const ParticlesComponent = (props) => {
  const options = useMemo(() => {
    return {
      interactivity: {
        events: {
          onHover: {
            enable: true,
            mode: "repulse",
          },
        },
        modes: {
          repulse: {
            distance: 100,
          },
        },
      },
      particles: {
        color: {
          value: "#F0F2F5",
        },
        number: {
          density: {
            enable: true,
            area: 1200,
          },
          value: 200,
        },
        links: {
          color: "#F0F2F5",
          enable: true,
        },
        move: {
          enable: true,
          speed: { min: 1, max: 3 },
        },
        size: {
          value: { min: 1, max: 3 },
        },
      },
    };
  }, []);

  const particlesInit = useCallback((engine) => {
    loadSlim(engine);

  }, []);

  return <Particles id={props.id} init={particlesInit} options={options} />;
};

export default ParticlesComponent;