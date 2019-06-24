package com.github.nbuesing.kafka.connect.opensky;


import com.github.nbuesing.kafka.connect.opensky.util.BoundingBoxUtil;
import com.github.nbuesing.kafka.connect.opensky.util.Validators;
import lombok.Getter;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.opensky.api.OpenSkyApi;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class OpenSkySourceConnectorConfig extends AbstractConfig {

//    public static final String X_CONFIG = "";
//    private static final String X_DEFAULT = "";
//    private static final String X_DISPLAY = "";
//    private static final String X_DOC = "";

    public static final String KAFKA_TOPIC_CONF = "topic";
    private static final String KAFKA_TOPIC_DOC = "the topic to write the opensky structure to";
    private static final String KAFKA_TOPIC_DISPLAY = "TBD";

    public static final String INTERVAL_CONF = "interval";
    private static final Long INTERVAL_DEFAULT = 30L;
    private static final String INTERVAL_DOC = "frequency, in seconds, in calling opensky api.";
    private static final String INTERVAL_DISPLAY = "TBD";

    public static final String BOUNDING_BOXES_CONF = "bounding.boxes";
    public static final String BOUNDING_BOXES_DEFAULT = "-90.0 90.0 -180.0 180.0";
    private static final String BOUNDING_BOXES_DOC = "regions to collect: [minLat maxLat maxLong maxLong], e.g. 45.84 47.82 5.99 10.52,24 49 -124 -66";
    private static final String BOUNDING_BOXES_DISPLAY = "TBD";

    public static final String OPENSKY_USERNAME_CONF = "opensky.username";
    private static final String OPENSKY_USERNAME_DOC = "opensky username, using an account allows for more frequent updates.";
    private static final String OPENSKY_USERNAME_DISPLAY = "TBD";

    public static final String OPENSKY_PASSWORD_CONF = "opensky.password";
    private static final String OPENSKY_PASSWORD_DOC = "opensky password, using an account allows for more frequent updates.";
    private static final String OPENSKY_PASSWORD_DISPLAY = "TBD";

    public OpenSkySourceConnectorConfig(final Map<String, String> parsedConfig) {
        super(conf(), parsedConfig);
    }

    public List<OpenSkyApi.BoundingBox> getBoundingBoxes() {
        return getList(BOUNDING_BOXES_CONF).stream().map(BoundingBoxUtil::toBoundingBox).collect(Collectors.toList());
    }

    public String getTopic() {
        return getString(KAFKA_TOPIC_CONF);
    }

    public Optional<Long> getInterval() {
        return Optional.ofNullable(getLong(INTERVAL_CONF));
    }

    public Optional<String> getOpenskyUsername() {
        return Optional.ofNullable(getString(OPENSKY_USERNAME_CONF));
    }

    public Optional<String> getOpenskyPassword() {
        return getPassword(OPENSKY_PASSWORD_CONF) != null ? Optional.of(getPassword(OPENSKY_PASSWORD_CONF).value()) : Optional.empty();
    }

    public static ConfigDef conf() {
        return new ConfigDef()
                .define(KAFKA_TOPIC_CONF, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, KAFKA_TOPIC_DOC)
                //.define(KAFKA_TOPIC_CONF, ConfigDef.Type.STRING, null, Validators.validTopic, ConfigDef.Importance.HIGH, KAFKA_TOPIC_DOC)
                .define(INTERVAL_CONF, ConfigDef.Type.LONG, INTERVAL_DEFAULT, ConfigDef.Range.atLeast(1), ConfigDef.Importance.MEDIUM, INTERVAL_DOC)
                .define(BOUNDING_BOXES_CONF, ConfigDef.Type.LIST, "-90 90 -180 180", Validators.validBoundingBoxes, ConfigDef.Importance.MEDIUM, BOUNDING_BOXES_CONF)
                .define(OPENSKY_USERNAME_CONF, ConfigDef.Type.STRING, null, ConfigDef.Importance.LOW, OPENSKY_USERNAME_DOC)
                .define(OPENSKY_PASSWORD_CONF, ConfigDef.Type.PASSWORD, null, ConfigDef.Importance.LOW, OPENSKY_PASSWORD_DOC);
    }
}
