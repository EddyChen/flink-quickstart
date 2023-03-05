#!/bin/bash

if [ "$1" = "help" ]; then
    echo "Usage: $(basename "$0") (jobmanager|${COMMAND_STANDALONE}|taskmanager|help)"
    exit 0
elif [ "$1" = "jobmanager" ]; then
    shift 1
    prepare_job_manager_start

    $FLINK_HOME/bin/jobmanager.sh start "$@"
elif [ "$1" = ${COMMAND_STANDALONE} ]; then
    shift 1
    prepare_job_manager_start

    $FLINK_HOME/bin/standalone-job.sh start "$@"
elif [ "$1" = "taskmanager" ]; then
    shift 1
    echo "Starting Task Manager"
    copy_plugins_if_required

    TASK_MANAGER_NUMBER_OF_TASK_SLOTS=${TASK_MANAGER_NUMBER_OF_TASK_SLOTS:-$(grep -c ^processor /proc/cpuinfo)}

    set_common_options
    set_config_option taskmanager.numberOfTaskSlots ${TASK_MANAGER_NUMBER_OF_TASK_SLOTS}

    if [ -n "${FLINK_PROPERTIES}" ]; then
        echo "${FLINK_PROPERTIES}" >> "${CONF_FILE}"
    fi
    envsubst < "${CONF_FILE}" > "${CONF_FILE}.tmp" && mv "${CONF_FILE}.tmp" "${CONF_FILE}"

    $FLINK_HOME/bin/taskmanager.sh start "$@"
fi

sleep 1
exec /bin/bash -c "tail -f $FLINK_HOME/log/*.log"